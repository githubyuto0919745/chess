package client;

public class ResponseException extends RuntimeException {
    public ResponseException(String message) {
        super(message);
    }

    public static ResponseException fromJson(String body) {
    }

    public static Object fromHttpStatusCode(int status) {
    }
}
