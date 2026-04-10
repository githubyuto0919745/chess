package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import record.*;
import server.websocket.WebSocketHandler;
import server.exceptions.AlreadyTakenException;
import server.exceptions.BadRequestException;
import server.exceptions.UnauthorizedException;

import java.io.IOException;
import java.util.HashMap;

public class Server {

    private final Javalin javalin;
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();


    public Server() {


        javalin = Javalin.create(config -> config.staticFiles.add("web"));


        javalin.exception(BadRequestException.class, (e,ctx) ->{
            ctx.status(400);
            ctx.result(new Gson().toJson( new ErrorMessage("Error: bad request")));
        });
        javalin.exception(UnauthorizedException.class, (e,ctx) ->{
            ctx.status(401);
            ctx.result(new Gson().toJson( new ErrorMessage("Error: unauthorized")));
        });
        javalin.exception(AlreadyTakenException.class, (e,ctx) ->{
            ctx.status(403);
            ctx.result(new Gson().toJson( new ErrorMessage("Error: already taken")));
        });
        javalin.exception(DataAccessException.class, (e,ctx) ->{
            ctx.status(500);
            ctx.result(new Gson().toJson( new ErrorMessage("Error: database failure")));
        });
        javalin.exception(Exception.class, (e,ctx) ->{
            ctx.status(500);
            ctx.result(new Gson().toJson( new ErrorMessage("Error: description error")));
        });


        javalin.delete("/db", this:: clearHandler);
        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
        javalin.delete("/session", this::logoutHandler);
        javalin.get("/game", this::listGameHandler);
        javalin.post("/game", this::createGameHandler);
        javalin.put("/game", this::joinGameHandler);



        javalin.ws("/ws", ws -> {
            ws.onConnect(webSocketHandler);
            ws.onMessage(webSocketHandler);
            ws.onClose(webSocketHandler);
        });
    }

    private void clearHandler(Context ctx)throws DataAccessException {
            ClearService clearservice = new ClearService();
            clearservice.clears();
            ctx.status(200);
}

    private void registerHandler(Context ctx)throws DataAccessException {
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

    }
    private void loginHandler(Context ctx) throws DataAccessException{


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
    }
    private void logoutHandler(Context ctx) throws DataAccessException{

            LogoutService logoutService = new LogoutService();
            String token = ctx.header("Authorization");
            if(token == null){
                throw new UnauthorizedException();
            }
            logoutService.logout(token);
            ctx.status(200);

    }

    private void listGameHandler(Context ctx) throws DataAccessException{

            ListGamesService listgamesService = new ListGamesService();
            String token = ctx.header("Authorization");
            if(token == null){
                throw new UnauthorizedException();
            }
            var games = listgamesService.listGames(token);
            HashMap<String, Object> response = new HashMap<>();
            response.put("games",games);
            ctx.result(new Gson().toJson(response));
            ctx.status(200);
    }

    private void createGameHandler(Context ctx)throws DataAccessException{


            CreateGameService createGameService = new CreateGameService();
            GameData game = new Gson().fromJson(ctx.body(), GameData.class);

            String token = ctx.header("Authorization");
            if(token == null){
                throw new UnauthorizedException();
            }
            if(game == null || game.gameName() == null){
                ErrorMessage error = new ErrorMessage("Error: bad request");
                ctx.result(new Gson().toJson(error));
                ctx.status(400);
                return;
            }
            GameData created = createGameService.createGames(game,token);
            ctx.status(200);
            ctx.result(new Gson().toJson(created));
    }

    private void joinGameHandler(Context ctx) throws DataAccessException, IOException {


            JoinGameService joinGameService = new JoinGameService();
            JoinGameRequest request = new Gson().fromJson(ctx.body(), JoinGameRequest.class);


            if(request == null ||request.gameID() == null){
                ErrorMessage error = new ErrorMessage("Error: bad request");
                ctx.result(new Gson().toJson(error));
                ctx.status(400);
                return;
            }
            String token = ctx.header("Authorization");
            if(token == null){
                throw new UnauthorizedException();
            }

            joinGameService.joinGame(token,request.gameID(), request.playerColor());
            ctx.status(200);
    }



    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }



    public void stop() {
        javalin.stop();
    }
}
