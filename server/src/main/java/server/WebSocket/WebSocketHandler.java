package server.WebSocket;


import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.websocket.*;
import org.eclipse.jetty.server.HttpChannelState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


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
//        try{
//            Action action = new Gson().fromJson(ctx.message(), Action.class);
//            switch (action.type()){
//                case ENTER -> enter(action.)
//            }
//            ctx.send("Websocket response: " + ctx.message());
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }

    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("Websocket closed");
    }


}
