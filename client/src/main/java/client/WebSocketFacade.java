package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import ui.BoardDesign;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class WebSocketFacade extends Endpoint {
    public Session session;
    private BoardDesign boardDesign;

    public static void main(String[] args) throws Exception{
        BoardDesign boardDesign = new BoardDesign("WHITE");
        WebSocketFacade client = new WebSocketFacade(boardDesign);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo:");
        while (true){
            client.send(scanner.nextLine());
        }
    }

    public WebSocketFacade(BoardDesign boardDesign) throws Exception{
        this.boardDesign = boardDesign;
            URI uri = new URI("ws://localhost:8080/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, uri);

    }

    public void handleServerMessage(String message){
        ServerMessage server = new Gson().fromJson(message, ServerMessage.class);
        switch(server.getServerMessageType()){
            case LOAD_GAME -> {
                ServerMessage.LoadGame loadGame = new Gson().fromJson(message, ServerMessage.LoadGame.class);
                returnLoadGame(loadGame);
            }
            case ERROR ->{
                ServerMessage.Error error = new Gson().fromJson(message, ServerMessage.Error.class);
                returnError(error);
            }
            case NOTIFICATION -> {
                ServerMessage.Notification notification = new Gson().fromJson(message, ServerMessage.Notification.class);
                returnNotification(notification);
            }

        }
    }

    public void returnLoadGame(ServerMessage.LoadGame server){

        System.out.println("Game Loaded");
        ChessGame game = server.getGame();
        boardDesign.updateGame(game);
        boardDesign.printBoard(null);
    }
    public void returnError(ServerMessage.Error server){

        System.out.println("Error" + server.getError());
    }
    public void returnNotification(ServerMessage.Notification server){
        System.out.println(server.getMessage());
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;

        this.session.addMessageHandler(new MessageHandler.Whole<String>(){
            public void onMessage(String message){
                handleServerMessage(message);
            }
        });
    }




    public void leave (String authToken, int gameID) throws ResponseException{
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }catch(IOException ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    public void move (String authToken, int gameID, ChessMove movement) throws ResponseException{
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID);
            action.move = movement;
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }catch(IOException ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    public void connect (String authToken, int gameID) throws ResponseException{
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }catch(IOException ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resign (String authToken, int gameID) throws ResponseException{
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }catch(IOException ex){
            throw new ResponseException(ex.getMessage());
        }
    }
    private void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

}
