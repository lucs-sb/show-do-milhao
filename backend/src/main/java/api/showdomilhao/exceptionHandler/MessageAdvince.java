package api.showdomilhao.exceptionHandler;

import api.showdomilhao.exceptionHandler.exceptions.MessageBadRequestException;
import api.showdomilhao.exceptionHandler.exceptions.MessageNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@ControllerAdvice(value = "api.showdomilhao.controller")
public class MessageAdvince {
    @ResponseBody
    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<MessageExceptionHandler> messageNotFound(MessageNotFoundException exception){
        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(MessageBadRequestException.class)
    public ResponseEntity<MessageExceptionHandler> messageBadRequest(MessageBadRequestException exception){
        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}