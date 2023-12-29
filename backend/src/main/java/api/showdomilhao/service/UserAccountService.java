package api.showdomilhao.service;

import api.showdomilhao.dto.HallDaFamaDTO;
import api.showdomilhao.dto.UserAccountDTO;
import api.showdomilhao.entity.Login;
import api.showdomilhao.entity.Role;
import api.showdomilhao.entity.UserAccount;
import api.showdomilhao.exceptionHandler.exceptions.MessageBadRequestException;
import api.showdomilhao.exceptionHandler.exceptions.MessageNotFoundException;
import api.showdomilhao.repository.LoginRepository;
import api.showdomilhao.repository.RoleRepository;
import api.showdomilhao.repository.UserAccountRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserAccountService {
    @Autowired
    private UserAccountRepository repository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${amazon.s3.bucket}")
    private String BUCKET;

    @Transactional(readOnly = true)
    public Optional<UserAccount> findByNickNameAndPassword(String nickname, String password) {
        Optional<UserAccount> user = Optional.ofNullable(repository.findUserByNickname(nickname).orElseThrow(() -> new MessageNotFoundException("Usuário não encontrado")));
        Optional<Login> login = loginRepository.findByNickname(nickname);
        if (new BCryptPasswordEncoder().matches(password, login.get().getPassword()))
            return user;
        else
            throw new MessageNotFoundException("Usuário não encontrado");
    }

    @Transactional(readOnly = true)
    public Optional<UserAccount> findById(Long id){
        return Optional.ofNullable(repository.findById(id).orElseThrow(() -> new MessageNotFoundException("Usuário não encontrado")));
    }

    @Transactional
    public void addUserAccount(String name, String nickname, String password, MultipartFile avatar) {
        Optional<UserAccount> userAccount = repository.findUserByNickname(nickname);
        if (userAccount.isPresent())
            throw new MessageBadRequestException("Usuário já existe");

        userAccount = Optional.of(new UserAccount());

        if (avatar != null)
            userAccount.get().setAvatar(saveFile(avatar));

        userAccount.get().setName(name);
        userAccount.get().setNickname(nickname);
        repository.save(userAccount.get());

        Login login = new Login();
        login.setUser(userAccount.get());
        login.setNickname(userAccount.get().getNickname());
        login.setPassword(passwordEncoder.encode(password));
        loginRepository.save(login);

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("USER");
        roles.add(role);

        login.setRoles(roles);
    }

    @Transactional
    public void updateUserById(Long id, String name, String nickname, String password, MultipartFile avatar) {
        Optional<UserAccount> userAccount = Optional.ofNullable(repository.findById(id).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        Optional<Login> login = Optional.of(loginRepository.findByNickname(userAccount.get().getNickname()).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        if (avatar != null)
            userAccount.get().setAvatar(saveFile(avatar));

        if (name != null && !name.isBlank())
            userAccount.get().setName(name);
        if (nickname != null && !nickname.isBlank() && repository.findUserByNickname(nickname).isEmpty())
            userAccount.get().setNickname(nickname);
        repository.save(userAccount.get());

        if (nickname != null && !nickname.isBlank())
            login.get().setNickname(userAccount.get().getNickname());
        if (password != null && !password.isBlank())
            login.get().setPassword(passwordEncoder.encode(password));
        loginRepository.save(login.get());
    }

    @Transactional
    public void deleteUserById(Long id) {
        Optional<UserAccount> userAccount = Optional.ofNullable(repository.findById(id).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        Optional<Login> login = Optional.of(loginRepository.findByNickname(userAccount.get().getNickname()).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        login.get().setDeletionDate(LocalDateTime.now());
        userAccount.get().setDeletionDate(LocalDateTime.now());

        loginRepository.save(login.get());
        repository.save(userAccount.get());
    }

    @Transactional(readOnly = true)
    public List<HallDaFamaDTO> getHalldaFama() {
        List<HallDaFamaDTO> hallDaFama = new ArrayList<>();
        List<UserAccount> user = repository.findHallDaFama();
        user.forEach(hall -> hallDaFama.add(new HallDaFamaDTO(hall.getUserId(), hall.getNickname(), hall.getTotalAward())));
        return hallDaFama;
    }

    private String saveFile(MultipartFile file){
        String ramdom = String.valueOf(UUID.randomUUID());

        try {
            amazonS3.putObject(new PutObjectRequest(BUCKET,
                    ramdom + "." + extractExtencion(file.getOriginalFilename()), file.getInputStream(),null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return "http://s3.amazonaws.com/"+BUCKET+"/" + ramdom + "." + extractExtencion(file.getOriginalFilename());
        } catch (IllegalStateException | IOException e) {
            throw new RuntimeException("Problemas na tentativa de salvar o arquivo");
        }
    }

    private String extractExtencion(String fileName){
        int i = fileName.lastIndexOf(".");
        return fileName.substring(i + 1);
    }
}
