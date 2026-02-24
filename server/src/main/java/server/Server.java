package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    ArrayList<String> users = new ArrayList<>();
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user/{username}", this::handler);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    private void handler(Context ctx){
        String username;
        String password;
        String email;

        String register = ctx.pathParam("username");
        users.add(register);
        list(ctx);

    }
    private void list(Context ctx){
        String jsonFormat = new Gson().toJson(Map.of("username", users));
        ctx.json(jsonFormat);
    }

    public void stop() {
        javalin.stop();
    }
}
