package api.showdomilhao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_user_account")
@Getter
@Setter
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String nickname;
    private String name;
    private String avatar;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "tb_validation_question_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> validationQuestions = new HashSet<>();
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "tb_validated_questions_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> validatedQuestions = new HashSet<>();

    public UserAccount(){}
}
