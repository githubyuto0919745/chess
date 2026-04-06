package server.WebSocket;


import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.websocket.*;
import jakarta.websocket.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

import javax.swing.*;
import java.io.IOException;

import static javax.management.remote.JMXConnectorFactory.connect;
import static websocket.commands.UserGameCommand.CommandType.CONNECT;


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
                case CONNECT -> connect(command.getAuthToken(), (Session) ctx.session);
                case MAKE_MOVE -> move(command.getAuthToken(), (Session) ctx.session);
                case LEAVE -> leave(command.getAuthToken(), (Session) ctx.session);
                case RESIGN -> resign(command.getAuthToken(), (Session) ctx.session);
            }
            ctx.send("Websocket response: " + ctx.message());
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private void connect (String authToken, Session session) throws IOException{

    }
    private void move (String authToken, Session session) throws IOException{

    }
    private void leave (String authToken, Session session) throws IOException{

    }
    private void resign (String authToken, Session session) throws IOException{

    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("Websocket closed");
    }


}
