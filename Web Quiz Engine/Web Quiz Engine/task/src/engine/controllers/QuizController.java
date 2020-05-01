package engine.controllers;

import engine.models.Answer;
import engine.models.Quiz;
import engine.models.Feedback;
import engine.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private QuizRepository quizzes;

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
    public ResponseEntity delete(@PathVariable String id) {
        var quiz = getQuiz(id);
        if (quiz.isPresent()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Quiz> post(@Valid @RequestBody Quiz quiz) {
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
