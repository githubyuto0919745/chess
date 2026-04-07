package server.WebSocket;


import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.websocket.*;
import jakarta.websocket.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.commands.messages.ServerMessage;

import javax.management.Notification;
import javax.swing.*;
import java.io.IOException;

import static websocket.commands.messages.ServerMessage.ServerMessageType.LOAD_GAME;
import static websocket.commands.messages.ServerMessage.ServerMessageType.ERROR;
import static websocket.commands.messages.ServerMessage.ServerMessageType.NOTIFICATION;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler{
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
                case MAKE_MOVE -> Move(command.getAuthToken(), (Session) ctx.session, command.getGameID());
                case LEAVE -> Leave(command.getAuthToken(), (Session) ctx.session, command.getGameID());
                case RESIGN -> Resign(command.getAuthToken(), (Session) ctx.session, command.getGameID());
            }
            ctx.send("Websocket response: " + ctx.message());
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }
    public void handleServerMessage(String message){
        ServerMessage server = new Gson().fromJson(message, ServerMessage.class);

        switch(server.getServerMessageType()){
            case LOAD_GAME -> LoadGame();
            case ERROR -> Error();
            case NOTIFICATION -> Notification();
            
        }
    }

    private void Notification() {
    }

    private void Error() {
    }

    private void LoadGame() {
    }

    private void Connect (String authToken, Session session, Integer gameID) throws IOException{

    }
    private void Move (String authToken, Session session, Integer gameID) throws IOException{

    }
    private void Leave (String authToken, Session session, Integer gameID) throws IOException{

    }
    private void Resign (String authToken, Session session, Integer gameID) throws IOException{

    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx, Integer gameID) throws Exception {
        System.out.println("Websocket closed");
    }


}
