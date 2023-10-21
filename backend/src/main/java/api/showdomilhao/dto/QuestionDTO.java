package api.showdomilhao.dto;

import api.showdomilhao.entity.Answer;
import api.showdomilhao.entity.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class QuestionDTO {

    private Long questionId;
    private Long userAccountId;
    private String statement;
    private boolean accepted;
    private int amountApprovals;
    private int amountFailures;
    private int amountComplaints;
    private Set<Answer> answers = new HashSet<>();

    public QuestionDTO(){}

    public QuestionDTO(Question question, Set<Answer> answerList){
        this.questionId = question.getQuestionId();
        this.userAccountId = question.getUserAccountId();
        this.statement = question.getStatement();
        this.accepted = question.isAccepted();
        this.amountApprovals = question.getAmountApprovals();
        this.amountFailures = question.getAmountFailures();
        this.amountComplaints = question.getAmountComplaints();
        this.answers = answerList;
    }
}
