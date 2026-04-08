package server.WebSocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.memory.AuthDAO;
import dataaccess.memory.GameDAO;
import io.javalin.Javalin;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import record.AuthData;
import record.GameData;
import websocket.commands.UserGameCommand;
import websocket.commands.messages.ServerMessage;


import java.io.IOException;


public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler{
    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO = new GameDAO();
    private final AuthDAO authDAO = new AuthDAO();
    public static void main( String[] args){
        Javalin.create()
                .get("/echo/{msg}",ctx->ctx.result("HTTP response: " + ctx.pathParam("msg")))
                .ws("/ws", ws -> {
                    ws.onConnect(ctx -> {
                        ctx.enableAutomaticPings();
                        System.out.println("Websocket connected");
                    });
                    ws.onMessage(ctx -> ctx.send ("WebSocket response: " + ctx.message()));
                    ws.onClose(ctx -> System.out.println("Websocket closed"));
                })
                .start(8080);
    }

    public void handleConnect (@NotNull WsConnectContext ctx){
        ctx.enableAutomaticPings();
        System.out.println("Websocket connected");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        ctx.send("WebSocket response: " + ctx.message());
        try{
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()){
                case CONNECT -> Connect((Session) ctx.session, command.getGameID());
                case MAKE_MOVE -> Move(command.getAuthToken(), (Session) ctx.session, command.getGameID(), command.move);
                case LEAVE -> Leave((Session) ctx.session, command.getGameID());
                case RESIGN -> Resign((Session) ctx.session, command.getGameID());
            }
            ctx.send("Websocket response: " + ctx.message());
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private void Connect (Session session, Integer gameID) throws IOException{
        connections.add(gameID, session);
        ServerMessage.Notification notification = new ServerMessage.Notification("You connected to a game");
        connections.broadcast(gameID, new Gson().toJson(notification));

    }
    private void Move (String authToken, Session session, Integer gameID, ChessMove move) throws IOException, InvalidMoveException {
        AuthData auth = authDAO.getAuth(authToken);
        String username = auth.username();
        GameData game = gameDAO.getGame(gameID);

        ChessGame chessGame = game.game();
        chessGame.makeMove(move);

        gameDAO.updateGame(game);

        ServerMessage.Notification notification = new ServerMessage.Notification(username + "You made a move");
        connections.broadcast(gameID, new Gson().toJson(notification));
    }
    private void Leave (Session session, Integer gameID) throws IOException{
        ServerMessage.Notification notification = new ServerMessage.Notification("You left the game");
        connections.broadcast(gameID, new Gson().toJson(notification));
        connections.remove(gameID, session);

    }
    private void Resign (Session session, Integer gameID) throws IOException{
        ServerMessage.Notification notification = new ServerMessage.Notification("You resigned the game");
        connections.broadcast(gameID, new Gson().toJson(notification));
        connections.remove(gameID, session);
    }






    private void Notification(Integer gameID) throws IOException {
        ServerMessage.Notification notification = new ServerMessage.Notification("You connected to a game");
        connections.broadcast(gameID, new Gson().toJson(notification));

    }

    private void Error(Session session) throws IOException {
        ServerMessage.Error error= new ServerMessage.Error("Error");
        session.getRemote().sendString(new Gson().toJson(error));
    }

    private void LoadGame(Integer gameID, ChessGame game) throws IOException {
        ServerMessage.LoadGame loadGame = new ServerMessage.LoadGame(game);
        connections.broadcast(gameID, new Gson().toJson(loadGame));

    }



    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {

    }
}
