package api.showdomilhao.dto;

import api.showdomilhao.entity.Answer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class QuestionDTO {

    private Long questionId;
    private Long userAccountId;
    private String statement;
    private Set<Answer> answers = new HashSet<>();

    public QuestionDTO(){}
}
