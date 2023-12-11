package api.showdomilhao.controller;

import api.showdomilhao.dto.MatchDTO;
import api.showdomilhao.entity.Match;
import api.showdomilhao.exceptionHandler.MessageExceptionHandler;
import api.showdomilhao.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Partida")
@RestController
@RequestMapping("/api/match")
public class MatchController {
    @Autowired
    private MatchService service;

    @Operation(summary = "Buscar partida pelo ID")
    @ApiResponse(responseCode = "200", description = "Buscou a partida", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Match.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Optional<Match>> findById(@PathVariable Long id) throws Exception{
        try {
            Optional<Match> matches = service.findById(id);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Buscar todas as partidas pelo ID do usuário")
    @ApiResponse(responseCode = "200", description = "Buscou as partidas", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Match.class)))
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<Match>> findAllByUserId(@PathVariable Long userId) throws Exception{
        try {
            List<Match> matches = service.findAllByUserId(userId);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Buscar todas as partidas encerradas ou em aberto pelo ID do usuário")
    @ApiResponse(responseCode = "200", description = "Buscou a(s) partida(s)", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Match.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<Match>> findByUserIdAndEnded(@RequestParam Long userId, @RequestParam boolean ended) throws Exception{
        try {
            List<Match> matches = service.findByUserIdAndEnded(userId, ended);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Criar uma nova partida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida criada", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Long> create(@RequestParam Long userId) throws Exception{
        try {
            Long id = service.create(userId);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Editar partida pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partida editada", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PutMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity update(@RequestBody MatchDTO match) throws Exception{
        try {
            service.update(match);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }
}
