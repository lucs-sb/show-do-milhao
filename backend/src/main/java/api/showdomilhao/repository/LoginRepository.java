package api.showdomilhao.repository;

import api.showdomilhao.entity.Login;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends CrudRepository<Login, Long> {

    @Query("SELECT * FROM tb_login WHERE nickname = :nickname AND deletion_date IS NULL")
    Optional<Login> findByNickname(String nickname);

    @Query("SELECT * FROM tb_login WHERE login_id = :id AND deletion_date IS NULL")
    Optional<Login> findById(Long id);
}
