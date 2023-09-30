package api.showdomilhao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("tb_validated_questions_user")
@Getter
@Setter
@AllArgsConstructor
public class ValidatedQuestionsUser {
    private Long questionId;
}