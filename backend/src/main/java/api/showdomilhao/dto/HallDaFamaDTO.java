package api.showdomilhao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HallDaFamaDTO {
    private Long userAccountId;
    private String nickname;
    private Long totalAward;

    public HallDaFamaDTO() {
    }
}
