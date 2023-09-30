package api.showdomilhao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("tb_question_answer")
@Getter
@Setter
@AllArgsConstructor
public class QuestionAnswer {
    private Long answerId;
}
