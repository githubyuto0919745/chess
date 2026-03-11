package dataaccess.mysql;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import passoff.exception.ResponseParseException;
import record.AuthData;
import record.UserData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDataAccess {
    public MySqlAuthDAO() throws DataAccessException {
        configureDatabase();
    }
    private final String[] createAuthTable = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `authToken` VARCHAR(256) NOT NULL,
            `username` VARCHAR(256) NOT NULL,
            FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE
            )
            """
    };
    private void configureDatabase() throws DataAccessException {
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
            var table = "SELECT username,authToken FROM auth WHERE username = ?";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1, authToken);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readAuth(rs);
                    }
                }
            }
        }catch( Exception e){
            throw new ResponseParseException("Unable",e);
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        String username  = rs.getString("username");
        String authToken = rs.getString("authToken");
        return new AuthData(username, authToken);
    }

    @Override
    public void createAuth(AuthData auth) {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "INSERT INTO auth (username,authToken) VALUES (?,?,?)";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1,auth.username());
                ps.setString(2,auth.authToken());
                ps.executeUpdate();
            }
        }catch( Exception e){
            throw new ResponseParseException("Unable",e);
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "DELETE FROM auth WHERE id = ?";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.executeUpdate();
            }
        }catch( Exception e){
            throw new ResponseParseException("Unable",e);
        }
    }

    @Override
    public void clear() {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "TRUNCATE auth";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.executeUpdate();
            }
        }catch( Exception e){
            throw new ResponseParseException("Unable",e);
        }
    }
}
