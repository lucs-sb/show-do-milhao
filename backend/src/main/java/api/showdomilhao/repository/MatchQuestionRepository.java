package api.showdomilhao.repository;

import api.showdomilhao.entity.MatchQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchQuestionRepository extends JpaRepository<MatchQuestion, Long> {
}
