package api.showdomilhao.repository;

import api.showdomilhao.entity.MatchAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchAnswerRepository extends JpaRepository<MatchAnswer, Long> {
}
