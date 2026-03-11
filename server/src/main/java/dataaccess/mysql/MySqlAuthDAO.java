package dataaccess.mysql;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import record.AuthData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDataAccess {
    public MySqlAuthDAO() throws SQLException, DataAccessException {
        configureDatabase();
    }


    private final String[] createAuthTable = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            `authToken` INT AUTO_INCREMENT PRIMARY KEY,
            `username` VARCHAR(256) NOT NULL,
            FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE
            )
            """
    };
    private void configureDatabase() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (Connection connect = DatabaseManager.getConnection()){
            for(String table : createAuthTable){
                try(var preparedStatement = connect.prepareStatement(table)){
                    preparedStatement.executeUpdate();
                }
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to configure table");

        }
    }



    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "SELECT authToken, ";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void createAuth(AuthData auth) {

    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() {

    }
}
