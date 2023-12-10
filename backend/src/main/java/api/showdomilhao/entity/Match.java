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
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "match")
    private Set<MatchQuestion> matchQuestions = new HashSet<>();
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "match")
    private Set<MatchAnswer> matchAnswers = new HashSet<>();

    public Match(){}
}
