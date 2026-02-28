package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import Record.*;

public class Server {

    private final Javalin javalin;

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this:: clearHandler);
        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
        javalin.delete("/session", this::logoutHandler);
    }

    private void clearHandler(Context ctx){
        ClearService clearservice = new ClearService();
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);
        GameData game = new Gson().fromJson(ctx.body(), GameData.class);

        clearservice.Clear(user, auth, game);
    }
    private void registerHandler(Context ctx) {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);

        RegisterService registerservice = new RegisterService();
        registerservice.register(user, auth);
    }
    private void loginHandler(Context ctx){
        LoginService loginService = new LoginService();
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);

        loginService.Login(user.username(), auth);
    }
    private void logoutHandler(Context ctx){
        LogoutService logoutService = new LogoutService();
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);

        logoutService.Logout(auth.authToken());
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }



    public void stop() {
        javalin.stop();
    }
}
