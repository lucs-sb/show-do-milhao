package api.showdomilhao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_match_answer")
@Getter
@Setter
public class MatchAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchAnswerId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    private Long answerId;
    private boolean deleted;

    public MatchAnswer() {
    }
}
