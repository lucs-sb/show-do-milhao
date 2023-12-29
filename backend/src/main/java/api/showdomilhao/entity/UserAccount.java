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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String nickname;
    private String name;
    private String avatar;
    private Long totalAward;
    private LocalDateTime deletionDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "tb_validation_question_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> validationQuestions = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "tb_validated_question_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> validatedQuestions = new HashSet<>();

    public UserAccount(){}
}
