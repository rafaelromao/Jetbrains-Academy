package engine.controllers;

import engine.models.Quiz;
import engine.models.Feedback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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
    public ResponseEntity<Quiz> post(@RequestBody Quiz quiz) {
        quiz.setId(quizzes.size());
        quizzes.add(quiz);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("{id}/solve")
    public ResponseEntity<Feedback> post(@PathVariable String id, @RequestParam String answer) {
        var quiz = getQuiz(id);
        if (quiz.isPresent()) {
            if (quiz.get().getAnswer() == Integer.parseInt(answer)) {
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
