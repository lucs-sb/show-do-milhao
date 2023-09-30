package api.showdomilhao.repository;

import api.showdomilhao.entity.Answer;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
    @Query("SELECT * FROM tb_answer WHERE question_id = :questionId ORDER BY RAND()")
    List<Answer> findAnswersByQuestionId(Long questionId);
}
