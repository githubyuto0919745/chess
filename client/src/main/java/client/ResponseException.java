package client;

import com.google.gson.Gson;
import record.ErrorMessage;

public class ResponseException extends RuntimeException {
    public ResponseException(String message) {
        super(message);
    }

    public ResponseException (String message, Throwable ex) {
        super(message, ex);
    }

    public static ResponseException fromJson( String body){
        try{
            ErrorMessage error = new Gson().fromJson(body, ErrorMessage.class);
            return new ResponseException(error.message());
        }catch(Exception e){
            return new ResponseException("Unknown error", e);
        }
    }
    public static String fromHttpStatusCode( int status){
        return switch(status){
            case 400 ->
                "Error: bad request";
            case 401 ->
                "Error: unauthorized";
            case 403 ->
                "Error: already exist";
            case 500 ->
                "Error: server error";
            default -> "Error:" + status;
        };
    }
}
