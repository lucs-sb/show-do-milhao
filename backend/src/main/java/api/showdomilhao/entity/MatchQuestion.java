package api.showdomilhao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("tb_match_question")
@Getter
@Setter
@AllArgsConstructor
public class MatchQuestion {
    private Long questionId;
    private int position;
}
