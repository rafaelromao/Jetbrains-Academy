package engine.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @Column
    private Integer id;
    @NotBlank
    @Column
    private String title;
    @NotBlank
    @Column
    private String text;
    @Column
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn
    @Size(min=2) @NotNull
    private List<QuizOption> options = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn
    private List<QuizAnswer> answers = new ArrayList<>();
}
