package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }


    public static class Notification extends ServerMessage{
        private String message;

        public Notification(String message) {
            super(ServerMessageType.NOTIFICATION);
            this.message = message;
        }
        public String getMessage(){
            return message;
        }
    }

    public static class LoadGame extends ServerMessage{
        private ChessGame game;

        public LoadGame(ChessGame game){
            super(ServerMessageType.LOAD_GAME);
            this.game = game;
        }
        public ChessGame getGame(){
            return game;
        }

    }
    public static class Error extends ServerMessage{
        private String error;

        public Error (String error){
            super(ServerMessageType.ERROR);
            this.error = error;
        }
        public String getError(){
            return error;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
