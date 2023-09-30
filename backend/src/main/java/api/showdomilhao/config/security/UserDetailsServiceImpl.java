package api.showdomilhao.config.security;

import api.showdomilhao.entity.Login;
import api.showdomilhao.exceptionHandler.exceptions.MessageNotFoundException;
import api.showdomilhao.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Optional<Login> login = loginRepository.findByNickname(nickname);
        if (login.isEmpty())
            throw new MessageNotFoundException("Usuário não encontrado");

        return new User(login.get().getUserAccountId().toString(), login.get().getPassword(), true, true, true, true, login.get().getAuthorities());
    }
}