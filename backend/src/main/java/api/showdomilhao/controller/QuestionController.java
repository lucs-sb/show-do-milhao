package api.showdomilhao.controller;

import api.showdomilhao.dto.QuestionDTO;
import api.showdomilhao.entity.Question;
import api.showdomilhao.exceptionHandler.MessageExceptionHandler;
import api.showdomilhao.service.QuestionService;
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

import java.util.*;

@Tag(name = "Pergunta")
@RestController
@RequestMapping("/api/question")
public class QuestionController {
    @Autowired
    private QuestionService service;

    @Operation(summary = "Buscar pergunta pelo ID")
    @ApiResponse(responseCode = "200", description = "Buscou a pergunta", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Question.class)))
    })
    @GetMapping("/{questionId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Optional<QuestionDTO>> findById(@PathVariable Long questionId) throws Exception{
        try {
            Optional<QuestionDTO> question = service.findQuestionById(questionId);
            return new ResponseEntity<>(question, HttpStatus.OK);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Buscar perguntas aceitas ou não aceitas pelo ID do usuário")
    @ApiResponse(responseCode = "200", description = "Buscou a partida", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Question.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<Question>> findQuestionsByUserIdAndAccepted(@RequestParam Long userId, @RequestParam boolean accepted) throws Exception{
        try {
            List<Question> questions = service.findQuestionsByUserIdAndAccepted(userId, accepted);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Buscar perguntas para aprovações pelo ID do usuário")
    @ApiResponse(responseCode = "200", description = "Buscou a partida", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Question.class)))
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<Question>> findQuestionsToApprovals(@PathVariable Long userId) throws Exception{
        try {
            List<Question> questions = service.findQuestionsToApprovals(userId);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Adicionar uma nova pergunta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pergunta criada", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class)))),
            @ApiResponse(responseCode = "400", description = "Lista de perguntas com número inferior ou acima de 4 respostas", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity addQuestion(@RequestBody QuestionDTO question) throws Exception{
        try {
              service.addQuestion(question);
              return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Editar pergunta pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pergunta editada", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Usuário, pergunta ou resposta não encontrado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PutMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity updateQuestion(@RequestBody QuestionDTO question) throws Exception{
        try {
            service.updateQuestion(question);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Denunciar pergunta pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pergunta editada", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Usuário ou pergunta não encontrado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PutMapping("/{questionId}/report")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity reportQuestion(@PathVariable Long questionId) throws Exception{
        try {
            service.reportQuestion(questionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Aprovar ou reprovar pergunta pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pergunta editada", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Usuário ou pergunta não encontrado", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @PutMapping("/{questionId}/validate")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity validateQuestion(@PathVariable Long questionId, @RequestParam Long userId, @RequestParam boolean validation) throws Exception{
        try {
            service.validateQuestion(questionId, userId, validation);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }

    @Operation(summary = "Apagar pergunta pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pergunta apagada", content = { @Content }),
            @ApiResponse(responseCode = "404", description = "Pergunta não encontrada", content =
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageExceptionHandler.class))))
    })
    @DeleteMapping("/{questionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity deleteQuestion(@PathVariable Long questionId) throws Exception{
        try {
            service.deleteQuestion(questionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception ex){
            throw new Exception(ex);
        }
    }
}
