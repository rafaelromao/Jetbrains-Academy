package engine.controllers;

import engine.models.Answer;
import engine.models.Quiz;
import engine.models.Feedback;
import engine.models.User;
import engine.repositories.QuizRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("api/quizzes")
public class QuizController {

    private final QuizRepository quizzes;

    public QuizController(QuizRepository quizzes) {
        this.quizzes = quizzes;
    }

    @GetMapping
    public ResponseEntity<Iterable<Quiz>> get() {
        return ResponseEntity.ok(quizzes.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Quiz> get(@PathVariable String id) {
        var quiz = getQuiz(id);
        if (quiz.isPresent()) {
            return ResponseEntity.ok(quiz.get());
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable String id, @AuthenticationPrincipal User user) {
        var quiz = getQuiz(id);
        if (quiz.isPresent()) {
            if (quiz.get().getAuthor().equals(user)) {
                quizzes.delete(quiz.get());
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Quiz> post(@Valid @RequestBody Quiz quiz, @AuthenticationPrincipal User user) {
        quiz.setAuthor(user);
        quiz = quizzes.save(quiz);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("{id}/solve")
    public ResponseEntity<Feedback> post(@PathVariable String id, @RequestBody Answer answer) {
        var quiz = getQuiz(id);
        if (quiz.isPresent()) {
            var providedAnswers = answer == null || answer.getAnswer() == null
                    ? new int[0]
                    : Arrays.stream(answer.getAnswer())
                    .distinct()
                    .toArray();
            var quizAnswers = quiz.get().getAnswer();
            var correctAnswers = quizAnswers == null
                    ? List.of()
                    : quizAnswers;
            if (correctAnswers.size() == providedAnswers.length &&
                    Arrays.stream(answer.getAnswer())
                            .filter(a -> correctAnswers.stream()
                                    .filter(v -> ((Integer)v).intValue() == a)
                                    .findAny()
                                    .isPresent())
                            .count() == correctAnswers.size()) {
                return ResponseEntity.ok(
                        new Feedback(true, "Congratulations, you're right!"));
            }
            return ResponseEntity.ok(
                    new Feedback(false, "Wrong answer! Please try again.")
            );
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    private Optional<Quiz> getQuiz(String id) {
        var idValue = Integer.parseInt(id);
        var quiz = quizzes.findById(idValue);
        return quiz;
    }

}
