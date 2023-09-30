package api.showdomilhao.repository;

import api.showdomilhao.dto.HallDaFamaDTO;
import api.showdomilhao.entity.UserAccount;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    @Query("SELECT * FROM tb_user_account WHERE nickname = :nickname " +
            "AND deletion_date IS NULL")
    Optional<UserAccount> findUserByNickname(String nickname);

    @Query("SELECT * FROM tb_user_account WHERE user_account_id = :id " +
            "AND deletion_date IS NULL")
    Optional<UserAccount> findById(Long id);

    @Query("SELECT ua.* FROM tb_user_account AS ua " +
            "INNER JOIN tb_validation_question_user AS vqu ON ua.user_account_id = vqu.user_account_id " +
            "WHERE vqu.question_id = :questionId AND ua.deletion_date IS NULL")
    List<UserAccount> findUsersByQuestionId(Long questionId);

    @Query("SELECT * FROM tb_user_account WHERE user_account_id != :id " +
            "AND deletion_date IS NULL ORDER BY RAND() LIMIT 5")
    List<UserAccount> findUsersToValidateQuestion(Long id);

    @Query("SELECT hall.name, hall.user_account_id, hall.total_award FROM " +
            "(SELECT ua.user_account_id, ua.name, SUM(m.award) AS total_award FROM tb_user_account AS ua " +
            "INNER JOIN tb_match AS m ON m.user_account_id = ua.user_account_id " +
            "WHERE ua.deletion_date IS NULL AND m.ended = 1 GROUP BY ua.name) hall " +
            "ORDER BY hall.total_award DESC LIMIT 10")
    List<HallDaFamaDTO> findHallDaFama();
}
