package engine.controllers;

import engine.models.Answer;
import engine.models.Quiz;
import engine.models.Feedback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Validated
@RestController
@RequestMapping("api/quizzes")
public class QuizController {

    private List<Quiz> quizzes = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Quiz>> get() {
        return ResponseEntity.ok(
                quizzes.stream().collect(toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Quiz> get(@PathVariable String id) {
        var quiz = getQuiz(id);
        if (quiz.isPresent()) {
            return ResponseEntity.ok(
                    quizzes.stream().filter(q -> q.getId() == Integer.parseInt(id)).findAny().get());
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
        quiz.setId(quizzes.size());
        quizzes.add(quiz);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("{id}/solve")
    public ResponseEntity<Feedback> post(@PathVariable String id, @RequestBody Answer answer) {
        var quiz = getQuiz(id);
        if (quiz.isPresent()) {
            var providedAnswers = answer == null || answer.getAnswer() == null
                    ? new int[0]
                    : Arrays.stream(answer.getAnswer()).distinct().toArray();
            var quizAnswers = quiz.get().getAnswer();
            var correctAnswers = quizAnswers == null
                    ? new int[0]
                    : quizAnswers;
            if (correctAnswers.length == providedAnswers.length &&
                    Arrays.stream(answer.getAnswer())
                            .filter(a -> Arrays.stream(correctAnswers)
                                    .filter(v -> v == a)
                                    .findAny()
                                    .isPresent())
                            .count() == correctAnswers.length) {
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
        var answerValue = Integer.parseInt(id);
        var quiz = quizzes.stream().filter(q -> q.getId() == answerValue).findAny();
        return quiz;
    }

}
