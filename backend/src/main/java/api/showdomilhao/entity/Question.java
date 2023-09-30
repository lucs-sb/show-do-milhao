package api.showdomilhao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table("tb_question")
@Getter
@Setter
public class Question {

    @Id
    private Long questionId;
    private Long userAccountId;
    private String statement;
    private boolean accepted;
    private int amountApprovals;
    private int amountFailures;
    private int amountComplaints;
    private LocalDateTime deletionDate;

    @MappedCollection(idColumn = "question_id")
    private Set<QuestionAnswer> answers = new HashSet<>();

    public Question(){}
}
