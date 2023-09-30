package api.showdomilhao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

@Table("tb_role")
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    private Long roleId;
    private String name;
    Long login_id;

    @Override
    public String getAuthority() {
        return name;
    }
}