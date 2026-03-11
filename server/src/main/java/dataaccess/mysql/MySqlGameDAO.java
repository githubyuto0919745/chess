package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDataAccess;
import passoff.exception.ResponseParseException;
import record.AuthData;
import record.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class MySqlGameDAO implements GameDataAccess {

    public MySqlGameDAO() throws DataAccessException {
        configureDatabase();
    }


    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS game (
            `gameID` INT AUTO_INCREMENT PRIMARY KEY,
            `gameName` VARCHAR(256) NOT NULL,
            `whiteUsername` VARCHAR(256),
            `blackUsername` VARCHAR(256),
            `game` TEXT NOT NULL,
            FOREIGN KEY (whiteUsername) REFERENCES user(username),
            FOREIGN KEY (blackUsername) REFERENCES user(username),
            )
            """
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection connect = DatabaseManager.getConnection()){
            for(String table : createTable){
                try(var preparedStatement = connect.prepareStatement(table)){
                    preparedStatement.executeUpdate();
                }
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to configure table");
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        Integer gameID  = rs.getInt("gameID");
        String gameName = rs.getString("gameName");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        ChessGame game = new ChessGame();

        return new GameData(gameID, gameName, whiteUsername, blackUsername, game);
    }


    @Override
    public GameData getGame(int gameID) {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "SELECT gameID, gameName, whiteUsername, blackUsername, game FROM game WHERE gameID = ?";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setInt(1, gameID);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readGame(rs);
                    }
                }
            }
        }catch( Exception e){
            throw new ResponseParseException("Unable",e);
        }
        return null;
    }

    @Override
    public GameData createGame(GameData game) {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "INSERT INTO game (gameName, whiteUsername, blackUsername, game) VALUES (?,?,?,?)";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setInt(1,game.gameID());
                ps.setString(2,game.gameName());
                ps.setString(3,game.whiteUsername());
                ps.setString(4,game.blackUsername());
                ps.executeUpdate();
            }
        }catch( Exception e){
            throw new ResponseParseException("Unable",e);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGame() {
        Collection<GameData> gameList = new ArrayList<>();
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "SELECT gameID, gameName, whiteUsername, blackUsername, game FROM game";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        gameList.add(readGame(rs));
                    }
                }
            }
        }catch( Exception e){
            throw new ResponseParseException("Unable",e);
        }
        return gameList;
    }

    @Override
    public void updateGame(GameData game) {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = """
                UPDATE game +
                SET gameName = ?, whiteUsername = ?, blackUsername= ?, game =?)
                WHERE gameID = ?
                """;
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setInt(1,game.gameID());
                ps.setString(2,game.gameName());
                ps.setString(3,game.whiteUsername());
                ps.setString(4,game.blackUsername());
                ps.executeUpdate();
            }
        }catch( Exception e) {
            throw new ResponseParseException("Unable", e);
        }
    }

    @Override
    public void clear() {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "TRUNCATE game";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.executeUpdate();
            }
        }catch( Exception e){
            throw new ResponseParseException("Unable",e);
        }
    }
}
