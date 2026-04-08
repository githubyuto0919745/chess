package client;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import org.eclipse.jetty.server.Server;
import websocket.commands.UserGameCommand;
import websocket.commands.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class WebSocketFacade extends Endpoint {
    public Session session;


    public static void main(String[] args) throws Exception{
        WebSocketFacade client = new WebSocketFacade();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo:");
        while (true){
            client.send(scanner.nextLine());
        }
    }

    public WebSocketFacade() throws Exception{

            URI uri = new URI("ws://localhost:8080/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, uri);
    }

    public void handleServerMessage(String message){
        ServerMessage server = new Gson().fromJson(message, ServerMessage.class);

        switch(server.getServerMessageType()){
            case LOAD_GAME -> {
                ServerMessage.LoadGame loadGame = new Gson().fromJson(message, ServerMessage.LoadGame.class);
                LoadGame(loadGame);
            }
            case ERROR ->{
                ServerMessage.Error error = new Gson().fromJson(message, ServerMessage.Error.class);
                Error(error);
            }
            case NOTIFICATION -> {
                ServerMessage.Notification notification = new Gson().fromJson(message, ServerMessage.Notification.class);
                Notification(notification);
            }

        }
    }

    public void LoadGame(ServerMessage.LoadGame server){
        System.out.println("Game Loaded");
    }
    public void Error(ServerMessage.Error server){
        System.out.println("Error" + server.getError());
    }
    public void Notification(ServerMessage.Notification server){
        System.out.println(server.getMessage());
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

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
