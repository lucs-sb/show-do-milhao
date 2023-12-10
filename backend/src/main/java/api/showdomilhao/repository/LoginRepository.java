package api.showdomilhao.repository;

import api.showdomilhao.entity.Login;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends CrudRepository<Login, Long> {

    @Query(value = "SELECT * FROM tb_login WHERE nickname = :nickname", nativeQuery = true)
    Optional<Login> findByNickname(String nickname);

    @Query(value = "SELECT * FROM tb_login WHERE login_id = :id", nativeQuery = true)
    Optional<Login> findById(Long id);
}
