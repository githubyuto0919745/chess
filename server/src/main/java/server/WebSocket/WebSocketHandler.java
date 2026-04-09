package server.WebSocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
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
    private final MySqlGameDAO gameDAO;
    private final MySqlAuthDAO authDAO;

    public WebSocketHandler(){
        try{
            this.authDAO =  new MySqlAuthDAO();
            this.gameDAO = new MySqlGameDAO();
        }catch(DataAccessException ex){
            throw new RuntimeException("Error");
        }

    }

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
        System.out.println("Websocket Connected");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        try{
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()){
                case CONNECT -> returnConnect(command.getAuthToken(), (Session) ctx.session, command.getGameID());
                case MAKE_MOVE -> returnMove(command.getAuthToken(), (Session) ctx.session, command.getGameID(), command.move);
                case LEAVE -> returnLeave((Session) ctx.session, command.getGameID());
                case RESIGN -> returnResign(command.getAuthToken(),(Session) ctx.session,command.getGameID());
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private void returnConnect (String authToken, Session session, Integer gameID) throws IOException, DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if(auth == null){
            sendError(session, "bad auth token");
            return;
        }
        String username = auth.username();
        GameData game = gameDAO.getGame(gameID);
        if(game == null){
            sendError(session,"bad game id");
            return;
        }
        ChessGame chessGame = game.game();
        connections.add(gameID, session);


        String role;
        if (username.equals(game.whiteUsername())) {
            role = "WHITE";
        } else if (username.equals(game.blackUsername())) {
            role = "BLACK";
        } else {
            role = "OBSERVER";
        }

        sendOnlyLoadGame(
                session,
                chessGame
        );
        sendNotificationExcept(gameID, username + " joined the game as " + role, session);

    }
    private void returnMove (String authToken, Session session, Integer gameID, ChessMove move) throws IOException, DataAccessException {

            AuthData auth = authDAO.getAuth(authToken);
            if(auth == null){
                sendError(session, "bad auth token for making move");
                return;
            }
            String username = auth.username();
            GameData game = gameDAO.getGame(gameID);
            ChessGame chessGame = game.game();

            if(chessGame.getGameOver()){
                sendError(session,"Game is already over");
                return;
            }
            String role;
            if (username.equals(game.whiteUsername())) {
                role = "WHITE";
            } else if (username.equals(game.blackUsername())) {
                role = "BLACK";
            } else {
                role = "OBSERVER";
            }
            if(role.equals("OBSERVER")){
                sendError(session, "Observers cannot make moves");
                return;
            }


            ChessGame.TeamColor currentTurn = chessGame.getTeamTurn();
            if((currentTurn == ChessGame.TeamColor.WHITE && !role.equals("WHITE")) ||
                    (currentTurn == ChessGame.TeamColor.BLACK && !role.equals("BLACK"))){
                return;
            }

            boolean found = false;
            for (ChessMove m : chessGame.validMoves(move.getStartPosition())){
                if(m.equals(move)){
                    found = true;
                    break;
                }
            }if(!found){
                return;
            }

            try{
                chessGame.makeMove(move);
            }catch(InvalidMoveException ex) {
                return;
            }

            gameDAO.updateGame(game);
            sendLoadGame(gameID,chessGame);
            sendNotificationExcept(gameID, username + " made move from " + move.getStartPosition() + " to " + move.getEndPosition(), session);


            if(chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)){
                sendNotificationAll(gameID,"White is in CheckMate!");
                return;
            }
            if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)){
                sendNotificationAll(gameID,"Black is in CheckMate!");
                return;
            }
            if(chessGame.isInStalemate(ChessGame.TeamColor.WHITE)){
                sendNotificationAll(gameID,"White is in StaleMate!");
                return;
            }
           if(chessGame.isInStalemate(ChessGame.TeamColor.BLACK)){
               sendNotificationAll(gameID,"Black is in StaleMate!");
               return;
            }
            if(chessGame.isInCheck(ChessGame.TeamColor.WHITE)) {
                sendNotificationAll(gameID,"White is in Check!");
            }
            if(chessGame.isInCheck(ChessGame.TeamColor.BLACK)){
                sendNotificationAll(gameID,"Black is in Check!");
            }
    }
    private void returnLeave (Session session, Integer gameID) throws IOException{
        sendNotificationExcept(gameID, "You left the game", session);
        connections.remove(gameID, session);

    }
    private void returnResign (String authToken, Session session, Integer gameID) throws IOException, DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if(auth == null){
            sendError(session, "bad auth token for making move");
            return;
        }
        String username = auth.username();
        GameData game = gameDAO.getGame(gameID);
        ChessGame chessGame = game.game();
        String role;
        if (username.equals(game.whiteUsername())) {
            role = "WHITE";
        } else if (username.equals(game.blackUsername())) {
            role = "BLACK";
        } else {
            role = "OBSERVER";
        }
        if(role.equals("OBSERVER")){
            sendError(session, "Observers cannot resign the game");
            return;
        }
        chessGame.resign();
        gameDAO.updateGame(game);
        sendNotificationAll(gameID,username + "You resigned the game");
    }

    public void sendNotificationExcept(Integer gameID, String message, Session exclude) throws IOException {
        ServerMessage.Notification notification = new ServerMessage.Notification(message);
        connections.broadcastExcept(gameID,exclude, new Gson().toJson(notification));
    }
    private void sendNotificationAll(Integer gameID, String message) throws IOException {
        ServerMessage.Notification notification = new ServerMessage.Notification(message);
        connections.broadcast(gameID, new Gson().toJson(notification));
    }



    private void sendError(Session session, String message) throws IOException {
        ServerMessage.Error error= new ServerMessage.Error(message);
        session.getRemote().sendString(new Gson().toJson(error));
    }

    private void sendLoadGame(Integer gameID, ChessGame game) throws IOException {
        ServerMessage.LoadGame loadGame = new ServerMessage.LoadGame(game);
        connections.broadcast(gameID, new Gson().toJson(loadGame));
    }
    private void sendOnlyLoadGame(Session session, ChessGame game) throws IOException {
        ServerMessage.LoadGame loadGame = new ServerMessage.LoadGame(game);
        connections.onePerson(session, new Gson().toJson(loadGame));
    }



    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {

    }
}
