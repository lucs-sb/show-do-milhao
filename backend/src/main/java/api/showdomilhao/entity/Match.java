package api.showdomilhao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table("tb_match")
@Getter
@Setter
public class Match {
    @Id
    private Long matchId;
    private Long userAccountId;
    private int award;
    private boolean ended;
    private int lastQuestionAnswered;
    private boolean deletedAnswers;
    private String reasonForClosing;
    @MappedCollection(idColumn = "match_id")
    private Set<MatchQuestion> questions = new HashSet<>();

    public Match(){}
}
