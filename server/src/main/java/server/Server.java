package server;

import com.google.gson.Gson;
import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import Record.*;

public class Server {
    UserDataAccess dataaccess = new UserDAO();
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
    }

    private void registerHandler(Context ctx) {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);

        RegisterService registerservice = new RegisterService();
        boolean register = registerservice.register(user, auth);

        if(register){
        ctx.json("registered");
        }
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
