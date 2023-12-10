package api.showdomilhao.repository;

import api.showdomilhao.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    @Query(value = "SELECT * FROM tb_question WHERE question_id = :questionId", nativeQuery = true)
    Optional<Question> findById(Long questionId);

    @Query(value = "SELECT q.* FROM tb_question AS q " +
            "INNER JOIN tb_validation_question_user AS vqu ON q.question_id = vqu.question_id " +
            "WHERE q.question_id = :questionId AND vqu.user_account_id = :userId", nativeQuery = true)
    Optional<Question> findByIdAndUserId(Long questionId, Long userId);

    @Query(value = "SELECT * FROM tb_question WHERE user_account_id = :userId " +
            "AND accepted = :accepted", nativeQuery = true)
    List<Question> findQuestionsByUserIdAndAccepted(Long userId, boolean accepted);

    @Query(value = "SELECT q.* FROM tb_question AS q " +
            "INNER JOIN tb_validation_question_user AS vqu ON q.question_id = vqu.question_id " +
            "WHERE vqu.user_account_id = :userId", nativeQuery = true)
    List<Question> findQuestionsToApprovals(Long userId);

    @Query(value = "SELECT * FROM tb_question WHERE accepted = 1" +
            " ORDER BY RAND() LIMIT 7", nativeQuery = true)
    List<Question> findByAcceptedForMatch();
}
