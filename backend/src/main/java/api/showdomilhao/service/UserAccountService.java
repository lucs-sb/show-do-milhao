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
    @Value("${directory.root}")
    private String rootDirectory;
    @Value("${directory.file}")
    private String fileDirectory;

    @Autowired
    private UserAccountRepository repository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        login.setUserAccountId(userAccount.get().getUserAccountId());
        login.setNickname(userAccount.get().getNickname());
        login.setPassword(passwordEncoder.encode(password));
        loginRepository.save(login);

        Role role = new Role();
        role.setName("USER");
        role.setLogin_id(login.getLoginId());
        roleRepository.save(role);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        login.setRoles(roles);
    }

    @Transactional
    public void updateUserById(Long id, UserAccountDTO newUserAccount) {
        Optional<UserAccount> userAccount = Optional.ofNullable(repository.findById(id).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        Optional<Login> login = Optional.of(loginRepository.findByNickname(userAccount.get().getNickname()).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        if (!newUserAccount.getName().isBlank())
            userAccount.get().setName(newUserAccount.getName());
        if (!newUserAccount.getNickname().isBlank() && repository.findUserByNickname(newUserAccount.getNickname()).isEmpty())
            userAccount.get().setNickname(newUserAccount.getNickname());
        repository.save(userAccount.get());

        if (!newUserAccount.getNickname().isBlank())
            login.get().setNickname(userAccount.get().getNickname());
        if (!newUserAccount.getPassword().isBlank())
            login.get().setPassword(passwordEncoder.encode(newUserAccount.getPassword()));
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

        userAccount.get().setDeletionDate(LocalDateTime.now());
        login.get().setDeletionDate(LocalDateTime.now());

        repository.save(userAccount.get());
        loginRepository.save(login.get());
    }

    @Transactional(readOnly = true)
    public List<HallDaFamaDTO> getHalldaFama() {
        return repository.findHallDaFama();
    }

    private String saveFile(MultipartFile file){
        String ramdom = String.valueOf(UUID.randomUUID());

        Path directoryPath = Paths.get(this.rootDirectory, this.fileDirectory);
        Path filePath = directoryPath.resolve(ramdom + "." + extractExtencion(Objects.requireNonNull(file.getOriginalFilename())));

        try {
            Files.createDirectories(directoryPath);
            file.transferTo(filePath.toFile());

            URI uri = filePath.toUri().resolve("http://localhost:8080/api/user/avatar/"+ramdom + "." + extractExtencion(file.getOriginalFilename()));

            return uri.toString();
        } catch (IOException e) {
            throw new RuntimeException("Problemas na tentativa de salvar o arquivo");
        }
    }

    private String extractExtencion(String fileName){
        int i = fileName.lastIndexOf(".");
        return fileName.substring(i + 1);
    }

//    public byte[] getAvatar(String name) throws IOException {
//        File file = new File(Objects.requireNonNull(UserAccountService.class.getClassLoader().getResource("arquivos/" + name)).getFile());
//
//        return readFile(file);
//    }
//
//    private byte[] readFile(File path) throws IOException {
//        try {
//            return Files.readAllBytes(Paths.get(path.getPath()));
//        }catch (IOException e){
//            throw new RuntimeException("Erro ao ler arquivo");
//        }
//    }
}
