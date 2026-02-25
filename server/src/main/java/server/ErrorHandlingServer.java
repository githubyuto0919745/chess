package server;

import io.javalin.Javalin;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;

public class ErrorHandlingServer {
    public static void main(String[] args){
        new ErrorHandlingServer().run();
    }

    private void run() {
        Javalin.create()
            .get("/error",this::throwException);
    }

    private void throwException(io.javalin.http.Context context) {
        throw new RuntimeException("The server has an error");
    }




}
