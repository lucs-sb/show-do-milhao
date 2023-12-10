package api.showdomilhao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_match")
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;
    private int award;
    private boolean ended;
    private int lastQuestionAnswered;
    private boolean deletedAnswers;
    private String reasonForClosing;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "tb_match_question",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> questions = new HashSet<>();

    public Match(){}
}
