package api.showdomilhao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table("tb_user_account")
@Getter
@Setter
public class UserAccount {

    @Id
    private Long userAccountId;
    private String nickname;
    private String name;
    private String avatar;
    private LocalDateTime deletionDate;
    @MappedCollection(idColumn = "user_account_id")
    private Set<ValidationQuestionUser> validationQuestions = new HashSet<>();
    @MappedCollection(idColumn = "user_account_id")
    private Set<ValidatedQuestionsUser> validatedQuestions = new HashSet<>();

    public UserAccount(){}
}
