package api.showdomilhao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("tb_match_answer")
@Getter
@Setter
@AllArgsConstructor
public class MatchAnswer {
    private long questionId;
    private long answerId;
    private boolean deleted;
}
