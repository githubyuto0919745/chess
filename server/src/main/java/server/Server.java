package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import Record.*;
import server.Exceptions.AlreadyTakenException;
import server.Exceptions.BadRequestException;
import server.Exceptions.UnauthorizedException;

import java.util.HashMap;

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

    private void clearHandler(Context ctx) {
        ClearService clearservice = new ClearService();

        try {
            clearservice.clears();
            ctx.status(200);
            ctx.json(new HashMap<>());
        } catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
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
        }catch(BadRequestException e){
            ErrorMessage error = new ErrorMessage("Error: already taken");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);
        }catch(AlreadyTakenException e){
            ErrorMessage error = new ErrorMessage("Error: already taken");
            ctx.result(new Gson().toJson(error));
            ctx.status(403);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
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
        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }
    private void logoutHandler(Context ctx){
        LogoutService logoutService = new LogoutService();
        String token = ctx.header("Authorization");
        try {
            logoutService.logout(token);
            ctx.status(200);
        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }

    private void listGameHandler(Context ctx){
        ListGamesService listgamesService = new ListGamesService();
        String token = ctx.header("Authorization");

        try {
            var games = listgamesService.listGames(token);
            HashMap<String, Object> response = new HashMap<>();
            response.put("games",games);
            ctx.result(new Gson().toJson(response));
            ctx.status(200);
        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }

    private void createGameHandler(Context ctx){
        CreateGameService createGameService = new CreateGameService();
        GameData game = new Gson().fromJson(ctx.body(), GameData.class);

        String token = ctx.header("Authorization");

        if(game == null || game.gameName() == null){
            ErrorMessage error = new ErrorMessage("Error: bad request");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }
        try {

            GameData create = createGameService.createGames(game,token);
            ctx.status(200);
            ctx.json(create);

        }catch(BadRequestException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);
        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }

    private void joinGameHandler(Context ctx){
        JoinGameService joinGameService = new JoinGameService();
        JoinGameRequest request = new Gson().fromJson(ctx.body(), JoinGameRequest.class);


        if(request == null || request.playerColor() == null){
            ErrorMessage error = new ErrorMessage("Error: bad request");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);
            return;
        }
        String token = ctx.header("Authorization");

        try {
            joinGameService.joinGame(token,request.gameID(),request.playerColor());
            ctx.result(new Gson().toJson(request.gameID()));
            ctx.status(200);
        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);

        }catch(BadRequestException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);

        }catch(AlreadyTakenException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(403);

        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }



    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }



    public void stop() {
        javalin.stop();
    }
}
