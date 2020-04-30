package engine.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quiz {
    private String title;
    private String text;
    private String[] options;
}
