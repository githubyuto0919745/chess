package server;

import com.google.gson.Gson;
import dataaccess.MemoryUserDataAccess;
import dataaccess.UserDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    UserDataAccess dataaccess = new MemoryUserDataAccess();
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
    }

    private void registerHandler(Context ctx){
        User user = new Gson().fromJson(ctx.body(),User.class);
        Service1 service = new Service1();
        service.register(user);
        ctx.json("registered");
    }
    private void loginHandler(Context ctx){

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }



    public void stop() {
        javalin.stop();
    }
}
