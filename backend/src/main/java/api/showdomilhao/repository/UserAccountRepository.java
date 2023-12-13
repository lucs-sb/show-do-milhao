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

    @Query(value = "SELECT * FROM tb_user_account WHERE user_id = :id", nativeQuery = true)
    Optional<UserAccount> findById(Long id);

    @Query(value = "SELECT ua.* FROM tb_user_account AS ua " +
            "INNER JOIN tb_validation_question_user AS vqu ON ua.user_id = vqu.user_id " +
            "WHERE vqu.question_id = :questionId", nativeQuery = true)
    List<UserAccount> findUsersByQuestionId(Long questionId);

    @Query(value = "SELECT * FROM tb_user_account WHERE user_id != :id" +
            " ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<UserAccount> findUsersToValidateQuestion(Long id);

    @Query(value = "SELECT * FROM tb_user_account ORDER BY total_award DESC LIMIT 10", nativeQuery = true)
    List<UserAccount> findHallDaFama();
}
