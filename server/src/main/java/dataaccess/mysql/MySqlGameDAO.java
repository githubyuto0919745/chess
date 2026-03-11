package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDataAccess;
import record.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
public class MySqlGameDAO implements GameDataAccess {

    public MySqlGameDAO() throws SQLException, DataAccessException {
        configureDatabase();
    }


    private final String[] createGameTable = {
            """
            CREATE TABLE IF NOT EXISTS game (
            `gameID` INT AUTO_INCREMENT PRIMARY KEY,
            `gameName` VARCHAR(256) NOT NULL,
            `whiteUsername` VARCHAR(256) NOT NULL,
            `blackUsername` VARCHAR(256) NOT NULL,
            FOREIGN KEY (whiteUsername) REFERENCES user(username),
            FOREIGN KEY (blackUsername) REFERENCES user(username),
            )
            """
    };
    private void configureDatabase() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (Connection connect = DatabaseManager.getConnection()){
            for(String table : createGameTable){
                try(var preparedStatement = connect.prepareStatement(table)){
                    preparedStatement.executeUpdate();
                }
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to configure table");

        }
    }


    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public GameData createGame(GameData game) {
        return null;
    }

    @Override
    public Collection<GameData> listGame() {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) {

    }

    @Override
    public void clear() {

    }
}
