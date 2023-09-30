package api.showdomilhao.repository;

import api.showdomilhao.entity.Match;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
    @Query("SELECT * FROM tb_match WHERE user_account_id = :id")
    List<Match> findAllByUserId(Long id);

    @Query("SELECT * FROM tb_match WHERE user_account_id = :id AND ended = :ended")
    List<Match> findByUserIdAndEnded(Long id, boolean ended);
}
