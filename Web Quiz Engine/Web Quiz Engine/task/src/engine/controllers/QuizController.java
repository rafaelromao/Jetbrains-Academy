package engine.controllers;

import engine.models.*;
import engine.repositories.CompletionRepository;
import engine.repositories.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("api/quizzes")
public class QuizController {

    private final QuizRepository quizzes;
    private final CompletionRepository completions;

    public QuizController(QuizRepository quizzes, CompletionRepository completions) {
        this.quizzes = quizzes;
        this.completions = completions;
    }

    @GetMapping
    public ResponseEntity<Page<Quiz>> get(@RequestParam int page) {
        var pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(quizzes.findAll(pageable));
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
            if (!quiz.get().getAuthor().equals(user)) {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            } else {
                quizzes.delete(quiz.get());
                return new ResponseEntity(HttpStatus.NO_CONTENT);
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
    public ResponseEntity<Feedback> solve(@PathVariable String id, @RequestBody Answer answer, @AuthenticationPrincipal User user) {
        var quiz = getQuiz(id);
        if (quiz.isPresent()) {
            var actualQuiz = quiz.get();
            var providedAnswers = answer == null || answer.getAnswer() == null
                    ? new int[0]
                    : Arrays.stream(answer.getAnswer())
                    .distinct()
                    .toArray();
            var quizAnswers = actualQuiz.getAnswer();
            var correctAnswers = quizAnswers == null
                    ? List.of()
                    : quizAnswers;
            if (correctAnswers.size() == providedAnswers.length &&
                    Arrays.stream(providedAnswers)
                            .filter(a -> correctAnswers.stream()
                                    .filter(v -> ((Integer) v).intValue() == a)
                                    .findAny()
                                    .isPresent())
                            .count() == correctAnswers.size()) {

                registerCompletion(user, actualQuiz);

                return ResponseEntity.ok(
                        new Feedback(true, "Congratulations, you're right!"));
            }
            return ResponseEntity.ok(
                    new Feedback(false, "Wrong answer! Please try again.")
            );
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("completed")
    public ResponseEntity<Page<Completion>> completed(@RequestParam int page, @AuthenticationPrincipal User user) {
        var pageable = PageRequest.of(page, 10);
        var items = completions.findAllCompletionsByUser(user.getId(), pageable);
        return ResponseEntity.ok(items);
    }

    private void registerCompletion(User user, Quiz quiz) {
        var completions = new ArrayList<Completion>();
        completions.addAll(quiz.getCompletions());

        var completion = new Completion();
        completion.setQuiz(quiz);
        completion.setSolver(user);
        completion = this.completions.save(completion);

        completions.add(completion);
        quiz.setCompletions(completions);
        quizzes.save(quiz);
    }

    private Optional<Quiz> getQuiz(String id) {
        var idValue = Integer.parseInt(id);
        var quiz = quizzes.findById(idValue);
        return quiz;
    }

}
