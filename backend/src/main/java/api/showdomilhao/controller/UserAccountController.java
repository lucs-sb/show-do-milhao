package api.showdomilhao.controller;

import api.showdomilhao.config.security.TokenService;
import api.showdomilhao.dto.AuthenticationDTO;
import api.showdomilhao.dto.HallDaFamaDTO;
import api.showdomilhao.dto.LoginResponseDTO;
import api.showdomilhao.dto.UserAccountDTO;
import api.showdomilhao.entity.Login;
import api.showdomilhao.entity.UserAccount;
import api.showdomilhao.exceptionHandler.MessageExceptionHandler;
import api.showdomilhao.exceptionHandler.exceptions.MessageBadRequestException;
import api.showdomilhao.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

@Tag(name = "Usuário")
@RestController
@RequestMapping("/api/user")
public class UserAccountController {
    @Autowired
    private UserAccountService service;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Autenticar usuário pelo nickname e senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LoginResponseDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) throws Exception{
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.nickname(), data.password());
            var auth = authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((Login) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token, ((Login) auth.getPrincipal()).getUserAccountId().toString()));
        }catch (Exception e){
            throw new MessageBadRequestException("Usuário ou senha inválidos");
        }
    }

    @Operation(summary = "Buscar usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buscou usuário", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserAccount.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Optional<UserAccount>> getUserById(@PathVariable Long id) throws Exception{
        try {
            Optional<UserAccount> user = service.findById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Operation(summary = "Adicionar novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Usuário já existe", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PostMapping
    public ResponseEntity addUser(@RequestParam String name, @RequestParam String nickname, @RequestParam String password, @RequestParam @Nullable MultipartFile avatar) throws Exception{
        try {
            service.addUserAccount(name, nickname, password, avatar);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Operation(summary = "Editar usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário editado", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody UserAccountDTO newUser) throws Exception{
        try {
            service.updateUserById(id, newUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Operation(summary = "Apagar usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário apagado", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity deleteUser(@PathVariable Long id) throws Exception{
        try {
            service.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Operation(summary = "Retorna os 10 melhores usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buscou o Hall da Fama", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserAccount.class)))),
            @ApiResponse(responseCode = "404", description = "Hall da Fama vazio", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @GetMapping("/hall-da-fama")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<HallDaFamaDTO>> getHalldaFama() throws Exception{
        try {
            List<HallDaFamaDTO> hallDaFama = service.getHalldaFama();
            return new ResponseEntity<>(hallDaFama, HttpStatus.OK);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Operation(summary = "Buscar foto do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buscou o arquivo com sucesso", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserAccount.class)))),
            @ApiResponse(responseCode = "500", description = "Erro ao ler o arquivo", content = { @Content })
    })
    @GetMapping("/avatar/{name}")
    public ResponseEntity getAvatar(@PathVariable String name) throws Exception{
        try {
            File file = new File(Objects.requireNonNull(UserAccountService.class.getClassLoader().getResource("arquivos/" + name)).getFile());
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(Objects.requireNonNull(UserAccountService.class.getClassLoader().getResourceAsStream("arquivos/" + name))));
        }catch (Exception e){
            throw new Exception(e);
        }
    }

//    @GetMapping("/avatar/{name}")
//    public ResponseEntity<byte[]> getAvatar(@PathVariable String name) throws Exception{
//        try {
//            byte[] file = service.getAvatar(name);
//            return new ResponseEntity<>(file, HttpStatus.OK);
//        }catch (Exception e){
//            throw new Exception(e);
//        }
//    }
}
