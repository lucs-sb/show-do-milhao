package api.showdomilhao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountDTO {
    private Long userAccountId;
    private String name;
    private String nickname;
    private String password;

    public UserAccountDTO(){}
}
