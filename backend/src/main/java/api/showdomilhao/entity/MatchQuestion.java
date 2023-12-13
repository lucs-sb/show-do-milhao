package api.showdomilhao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_match_question")
@Getter
@Setter
public class MatchQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchQuestionId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    private Long questionId;
    private int position;

    public MatchQuestion(){}
}
