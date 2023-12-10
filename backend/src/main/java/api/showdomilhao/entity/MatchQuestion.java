package api.showdomilhao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_match_question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchQuestionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
    private int position;

    public MatchQuestion(Question question, int position) {
        this.question = question;
        this.position = position;
    }
}
