package api.showdomilhao.exceptionHandler.exceptions;

public class MessageNotFoundException extends RuntimeException {
    private String message;

    public MessageNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}