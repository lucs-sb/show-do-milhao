package api.showdomilhao.repository;

import api.showdomilhao.dto.HallDaFamaDTO;
import api.showdomilhao.entity.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    @Query(value = "SELECT * FROM tb_user_account WHERE nickname = :nickname", nativeQuery = true)
    Optional<UserAccount> findUserByNickname(String nickname);

    @Query(value = "SELECT * FROM tb_user_account WHERE user_account_id = :id", nativeQuery = true)
    Optional<UserAccount> findById(Long id);

    @Query(value = "SELECT ua.* FROM tb_user_account AS ua " +
            "INNER JOIN tb_validation_question_user AS vqu ON ua.user_account_id = vqu.user_account_id " +
            "WHERE vqu.question_id = :questionId", nativeQuery = true)
    List<UserAccount> findUsersByQuestionId(Long questionId);

    @Query(value = "SELECT * FROM tb_user_account WHERE user_account_id != :id" +
            " ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<UserAccount> findUsersToValidateQuestion(Long id);

    @Query(value = "SELECT hall.nickname, hall.user_account_id, hall.total_award FROM " +
            "(SELECT ua.user_account_id, ua.nickname, SUM(m.award) AS total_award FROM tb_user_account AS ua " +
            "INNER JOIN tb_match AS m ON m.user_account_id = ua.user_account_id " +
            "WHERE m.ended = 1 GROUP BY ua.nickname) hall " +
            "ORDER BY hall.total_award DESC LIMIT 10", nativeQuery = true)
    List<HallDaFamaDTO> findHallDaFama();
}
