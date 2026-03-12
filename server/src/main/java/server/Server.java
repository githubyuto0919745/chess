package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import record.*;
import server.exceptions.AlreadyTakenException;
import server.exceptions.BadRequestException;
import server.exceptions.UnauthorizedException;

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
        try {
            ClearService clearservice = new ClearService();
            clearservice.clears();
            ctx.status(200);
        }catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: database failure");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
        catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }

    }

    private void registerHandler(Context ctx) {

        try {
            RegisterService registerservice = new RegisterService();
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);
            if(user == null || user.username()==null || user.password() == null) {
                ErrorMessage error = new ErrorMessage("Error: bad request");
                ctx.result(new Gson().toJson(error));
                ctx.status(400);
                return;
            }
            AuthData auth = registerservice.register(user);
            ctx.result(new Gson().toJson(auth));
            ctx.status(200);
        }catch(AlreadyTakenException e){
            ErrorMessage error = new ErrorMessage("Error: already taken");
            ctx.result(new Gson().toJson(error));
            ctx.status(403);
        }catch(BadRequestException e){
            ErrorMessage error = new ErrorMessage("Error: bad request");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);
        }catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: database failure");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }

    }
    private void loginHandler(Context ctx){

        try {
            LoginService loginService = new LoginService();
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);
            if(user == null || user.username()==null || user.password() == null){
                ErrorMessage error = new ErrorMessage("Error: bad request");
                ctx.result(new Gson().toJson(error));
                ctx.status(400);
                return;
            }
            AuthData auth = loginService.login(user.username(), user.password());
            ctx.result(new Gson().toJson(auth));
            ctx.status(200);
        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: database failure");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }
    private void logoutHandler(Context ctx){
        try {
            LogoutService logoutService = new LogoutService();
            String token = ctx.header("Authorization");
            logoutService.logout(token);
            ctx.status(200);
        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: database failure");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }

    private void listGameHandler(Context ctx){
        try {
            ListGamesService listgamesService = new ListGamesService();
            String token = ctx.header("Authorization");
            var games = listgamesService.listGames(token);
            HashMap<String, Object> response = new HashMap<>();
            response.put("games",games);
            ctx.result(new Gson().toJson(response));
            ctx.status(200);
        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: database failure");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }

    private void createGameHandler(Context ctx){

        try {
            CreateGameService createGameService = new CreateGameService();
            GameData game = new Gson().fromJson(ctx.body(), GameData.class);

            String token = ctx.header("Authorization");

            if(game == null || game.gameName() == null){
                ErrorMessage error = new ErrorMessage("Error: bad request");
                ctx.result(new Gson().toJson(error));
                ctx.status(400);
                return;
            }
            GameData created = createGameService.createGames(game,token);
            ctx.status(200);
            ctx.result(new Gson().toJson(created));

        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);
        }catch(BadRequestException e){
            ErrorMessage error = new ErrorMessage("Error: bad request");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);
        }catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: database failure");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }catch (Exception e){
            ErrorMessage error = new ErrorMessage("Error: description of error");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
        }
    }

    private void joinGameHandler(Context ctx){

        try {
            JoinGameService joinGameService = new JoinGameService();
            JoinGameRequest request = new Gson().fromJson(ctx.body(), JoinGameRequest.class);


            if(request == null || request.playerColor() == null|| request.gameID() == null){
                ErrorMessage error = new ErrorMessage("Error: bad request");
                ctx.result(new Gson().toJson(error));
                ctx.status(400);
                return;
            }
            String token = ctx.header("Authorization");

            joinGameService.joinGame(token,request.gameID(),request.playerColor());
            ctx.status(200);
        }catch(AlreadyTakenException e){
            ErrorMessage error = new ErrorMessage("Error: already taken");
            ctx.result(new Gson().toJson(error));
            ctx.status(403);

        }catch(UnauthorizedException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            ctx.result(new Gson().toJson(error));
            ctx.status(401);

        }catch(BadRequestException e){
            ErrorMessage error = new ErrorMessage("Error: bad request");
            ctx.result(new Gson().toJson(error));
            ctx.status(400);

        }catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: database failure");
            ctx.result(new Gson().toJson(error));
            ctx.status(500);
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
