package client;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import io.javalin.router.Endpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketContainer;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade (String url, NotificationHandler notificationHandler) throws ResponseException{
        try{
            url = url.replace("http","ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
        }catch(DeploymentException | IOException | URISyntaxException ex){
            throw new ResponseException(ResponseException.getMessage(ex));
        }
    }

    public void leave (String authToken) throws ResponseException{
        try{
            var action = new Action(Action.Type.Exit, visitorName );
            this.session.getBasicRemote().sendText(new Gson().toJson(action));

        }
    }
}
