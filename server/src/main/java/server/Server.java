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
        javalin.get("/game", this::listGameHandler);
        javalin.post("/game", this::createGameHandler);
        javalin.put("/game", this::joinGameHandler);
    }

    private void clearHandler(Context ctx){
        ClearService clearservice = new ClearService();
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);
        GameData game = new Gson().fromJson(ctx.body(), GameData.class);

        clearservice.clears(user, auth, game);
    }
    private void registerHandler(Context ctx) {
        RegisterService registerservice = new RegisterService();
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        if(user == null || user.username()==null || user.password() == null) {
            ErrorMessage error = new ErrorMessage("Error: bad request");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);
            return;
        }
        try {
            AuthData auth = new AuthData(user.username(), user.username()+"_token");
            registerservice.register(user, auth);
            ctx.result(new Gson().toJson(auth));
            ctx.status(200);
        }catch(RuntimeException e){
            ErrorMessage error = new ErrorMessage("Error: already taken");
            ctx.result(new Gson().toJson(error));
            ctx.status(403);
        }

    }
    private void loginHandler(Context ctx){
        LoginService loginService = new LoginService();
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        if(user == null || user.username()==null || user.password() == null){
            ErrorMessage error = new ErrorMessage("Error: bad request");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);
            return;
        }
        AuthData auth = new AuthData( user.username(), user.username()+"_token");

        try {
            loginService.login(user.username(), user.password(), auth);
            ctx.result(new Gson().toJson(auth));
            ctx.status(200);
        }catch(RuntimeException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }
    }
    private void logoutHandler(Context ctx){
        LogoutService logoutService = new LogoutService();
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);

        logoutService.logout(auth.authToken());
    }

    private void listGameHandler(Context ctx){
        ListGamesService listgamesService = new ListGamesService();
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);

        listgamesService.listGame(auth.authToken());
    }

    private void createGameHandler(Context ctx){
        CreateGameService createGameService = new CreateGameService();
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);
        GameData game = new Gson().fromJson(ctx.body(), GameData.class);

        createGameService.createGames(game,auth.authToken());
    }

    private void joinGameHandler(Context ctx){
        JoinGameService joinGameService = new JoinGameService();
        AuthData auth = new Gson().fromJson(ctx.body(), AuthData.class);
        GameData game = new Gson().fromJson(ctx.body(), GameData.class);

        joinGameService.joinGame(auth.authToken(), game.gameID(), auth.username());
    }



    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }



    public void stop() {
        javalin.stop();
    }
}
