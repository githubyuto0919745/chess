package client;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

@ClientEndpoint

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

            URI uri = new URI(" ws://localhost:8080/ws");
            WebSocketContainer container = (WebSocketContainer) ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, uri);
    }

    public void leave (String authToken) throws ResponseException{
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }catch(IOException ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    public void move (String authToken) throws ResponseException{
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }catch(IOException ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    public void connect (String authToken) throws ResponseException{
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }catch(IOException ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resign (String authToken) throws ResponseException{
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }catch(IOException ex){
            throw new ResponseException(ex.getMessage());
        }
    }
    private void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
    public void onOpen (Session session, EndpointConfig endpointConfig){
    }
}
