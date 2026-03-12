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
            throw new DataAccessException("Unable to configure table", ex);
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
    public GameData getGame(int gameID) throws DataAccessException{
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
        }catch(SQLException ex){
            throw new DataAccessException("Unable to get Game", ex);
        }
        return null;
    }

    @Override
    public GameData createGame(GameData game)throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "INSERT INTO game (gameName, whiteUsername, blackUsername, game) VALUES (?,?,?,?)";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1,game.gameName());
                ps.setString(2,game.whiteUsername());
                ps.setString(3,game.blackUsername());
                ps.setString(4, "");
                ps.executeUpdate();
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to create Game", ex);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGame()throws DataAccessException {
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
        }catch(SQLException ex){
            throw new DataAccessException("Unable to list Game", ex);
        }
        return gameList;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException{
        try (Connection connect = DatabaseManager.getConnection()){
            var table = """
                UPDATE game
                SET gameName = ?, whiteUsername = ?, blackUsername= ?, game =?
                WHERE gameID = ?
        """;
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1,game.gameName());
                ps.setString(2,game.whiteUsername());
                ps.setString(3,game.blackUsername());
                ps.setString(4,"");
                ps.setInt(5,game.gameID());
                ps.executeUpdate();
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to update Game", ex);
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "DELETE FROM game";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.executeUpdate();
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to clear Game", ex);
        }
    }
}
