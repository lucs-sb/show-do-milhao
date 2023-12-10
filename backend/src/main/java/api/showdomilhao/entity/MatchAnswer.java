package api.showdomilhao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_match_answer")
@Getter
@Setter
public class MatchAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchAnswerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;
    private boolean deleted;

    public MatchAnswer(Answer answer, boolean deleted) {
        this.answer = answer;
        this.deleted = deleted;
    }

    public MatchAnswer() {
    }
}
