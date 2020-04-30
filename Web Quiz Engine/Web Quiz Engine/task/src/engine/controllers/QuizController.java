package engine.controllers;

import engine.models.Quiz;
import engine.models.Feedback;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/quiz")
public class QuizController {

    @GetMapping
    public Quiz get() {
        return new Quiz("title", "text", new String[]{"1", "2", "correct", "4"});
    }

    @PostMapping
    public Feedback post(@RequestParam String answer) {
        if ("2".equals(answer)) {
            return new Feedback(true, "Congratulations, you're right!");
        }
        return new Feedback(false, "Wrong answer! Please try again.");
    }

}
