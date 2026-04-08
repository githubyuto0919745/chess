package server.WebSocket;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session){
        Set<Session> sessions = connections.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet());

        sessions.add(session);
    }
    public void remove(Integer gameID, Session session){
        Set<Session> sessions = connections.get(gameID);
        if(sessions != null){
            sessions.remove(session);
            if(sessions.isEmpty()){
                connections.remove(gameID);
            }
        }
    }
    public void broadcast(Integer gameID, String notification) throws IOException{
        Set<Session> sessions = connections.get(gameID);
        if(sessions != null){
            for(Session session : sessions){
                if(session.isOpen()){
                    session.getRemote().sendString(notification);
                }
            }
        }

    }
    public void broadcastExcept (Integer gameID, Session exclude, String message) throws IOException {
        Set<Session> sessions = connections.get(gameID);
        if(sessions != null){
            for (Session session : sessions){
                if(session.isOpen() && !session.equals(exclude)){
                    session.getRemote().sendString(message);
                }
            }
        }
    }
    public void onePerson (Session only, String message) throws IOException {
            if (only !=null && only.isOpen()) {
                only.getRemote().sendString(message);
            }
    }

}




