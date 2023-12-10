package api.showdomilhao.repository;

import api.showdomilhao.entity.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
    @Query(value = "SELECT * FROM tb_match WHERE user_account_id = :id", nativeQuery = true)
    List<Match> findAllByUserId(Long id);

    @Query(value = "SELECT * FROM tb_match WHERE user_account_id = :id AND ended = :ended", nativeQuery = true)
    List<Match> findByUserIdAndEnded(Long id, boolean ended);
}
