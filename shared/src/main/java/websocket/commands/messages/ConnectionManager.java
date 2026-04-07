package websocket.commands.messages;
import org.eclipse.jetty.websocket.api.Session;

import javax.management.Notification;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Session> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session){
        connections.put(gameID, session);
    }
    public void remove(Integer gameID, Session session){
        connections.remove(gameID, session);
    }
    public void broadcast(Session excludeSession, Notification notification) throws IOException{
        String msg = notification.toString();
        for (Session session : connections.values()){
            if(session.isOpen()){
                if(!session.equals(excludeSession)){
                    session.getRemote().sendString(msg);
                }
            }
        }
    }
}
