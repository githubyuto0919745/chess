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
import websocket.messages.ServerMessage;


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
                case CONNECT -> Connect(command.getAuthToken(), (Session) ctx.session, command.getGameID());
                case MAKE_MOVE -> Move(command.getAuthToken(), (Session) ctx.session, command.getGameID(), command.move);
                case LEAVE -> Leave((Session) ctx.session, command.getGameID());
                case RESIGN -> Resign(command.getGameID());
            }
            ctx.send("Websocket response: " + ctx.message());
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private void Connect (String authToken, Session session, Integer gameID) throws IOException{
        AuthData auth = authDAO.getAuth(authToken);
        String username = auth.username();
        GameData game = gameDAO.getGame(gameID);
        ChessGame chessGame = game.game();
        connections.add(gameID, session);

        String role;
        if(username.equals(game.whiteUsername())){
            role = "WHITE";
        }else if(username.equals(game.blackUsername())){
            role = "BLACK";
        }else{
            role = "OBSERVE";
        }
        sendLoadGame(gameID,chessGame);
        sendNotificationExcept(gameID,username + " joined the game as " + role);
    }
    private void Move (String authToken, Session session, Integer gameID, ChessMove move) throws IOException, InvalidMoveException {
        try{
            AuthData auth = authDAO.getAuth(authToken);
            String username = auth.username();
            GameData game = gameDAO.getGame(gameID);

            ChessGame chessGame = game.game();
            chessGame.makeMove(move);

            gameDAO.updateGame(game);
            sendLoadGame(gameID,chessGame);
            sendNotificationExcept(gameID, username + " moved from" + move.getStartPosition() + " to " + move.getEndPosition());

            if(chessGame.isInCheck(ChessGame.TeamColor.WHITE) ||
            chessGame.isInCheck(ChessGame.TeamColor.BLACK)){
                sendNotificationAll(gameID,"Check!");
            }
            if(chessGame.isInCheckmate(ChessGame.TeamColor.WHITE) ||
                    chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)){
                sendNotificationAll(gameID,"CheckMate!");
            }
            if(chessGame.isInStalemate(ChessGame.TeamColor.WHITE) ||
                    chessGame.isInStalemate(ChessGame.TeamColor.BLACK)){
                sendNotificationAll(gameID,"StaleMate!");
            }
        }catch(InvalidMoveException ex) {
            sendError(session, "error");
        }
    }
    private void Leave (Session session, Integer gameID) throws IOException{
        sendNotificationExcept(gameID, "You left the game");
        connections.remove(gameID, session);

    }
    private void Resign (Integer gameID) throws IOException{
        sendNotificationAll(gameID,"You resigned the game");

    }

    private void sendNotificationExcept(Integer gameID, String message) throws IOException {
        ServerMessage.Notification notification = new ServerMessage.Notification(message);
        connections.broadcastExcept(gameID, new Gson().toJson(notification));
    }
    private void sendNotificationAll(Integer gameID, String message) throws IOException {
        ServerMessage.Notification notification = new ServerMessage.Notification(message);
        connections.broadcast(gameID, new Gson().toJson(notification));
    }

    private void sendError(Session session, String message) throws IOException {
        ServerMessage.Error error= new ServerMessage.Error("Error" + message);
        session.getRemote().sendString(new Gson().toJson(error));
    }

    private void sendLoadGame(Integer gameID, ChessGame game) throws IOException {
        ServerMessage.LoadGame loadGame = new ServerMessage.LoadGame(game);
        connections.broadcast(gameID, new Gson().toJson(loadGame));

    }



    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {

    }
}
