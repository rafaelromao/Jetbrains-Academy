package engine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Completion {
    @Id
    @Column
    @GeneratedValue
    @JsonIgnore
    private Integer id;
    @JsonIgnore
    @ManyToOne
    private User solver;
    @JsonIgnore
    @ManyToOne
    private Quiz quiz;

    @NotNull
    @Column
    private LocalDateTime completedAt = LocalDateTime.now();
    @JsonProperty("id")
    private Integer getQuizId() {
        return quiz.getId();
    }
}
