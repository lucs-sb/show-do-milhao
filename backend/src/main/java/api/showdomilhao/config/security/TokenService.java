package api.showdomilhao.config.security;

import api.showdomilhao.entity.Login;
import api.showdomilhao.exceptionHandler.exceptions.MessageBadRequestException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.show_do_milhao.token.secret}")
    private String secret;

    public String generateToken(Login user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("show-do-milhao")
                    .withSubject(user.getNickname())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException ex){
            throw new MessageBadRequestException("Erro ao gerar o token de autenticação");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("show-do-milhao")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException ex){
            throw new MessageBadRequestException("Usuário não autenticado");
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
