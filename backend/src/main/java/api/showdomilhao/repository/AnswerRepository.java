package api.showdomilhao.repository;

import api.showdomilhao.entity.Answer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
    @Query(value = "SELECT a.* FROM tb_answer AS a " +
            "INNER JOIN tb_question_answer AS qa ON qa.answer_id = a.answer_id " +
            "INNER JOIN tb_match_answer AS ma ON ma.answer_id = qa.answer_id " +
            "WHERE qa.question_id = :questionId AND ma.match_id = :matchId AND ma.deleted = 0 " +
            "ORDER BY RAND()", nativeQuery = true)
    Set<Answer> findAnswersByMatchIdAndQuestionId(Long matchId, Long questionId);
}
