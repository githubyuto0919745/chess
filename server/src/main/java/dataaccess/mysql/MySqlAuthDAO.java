package dataaccess.mysql;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import passoff.exception.ResponseParseException;
import record.AuthData;
import record.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDataAccess {
    public MySqlAuthDAO() throws DataAccessException {
        MySqlBaseDAO.configureDatabase(CREATE_TABLE);
    }
    private final String[] CREATE_TABLE = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `authToken` VARCHAR(256) NOT NULL,
            `username` VARCHAR(256) NOT NULL
            )
            """
    };

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "SELECT username, authToken FROM auth WHERE authToken = ?";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1, authToken);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readAuth(rs);
                    }
                }
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to get Auth", ex);
        }
        return null;
    }
    private AuthData readAuth(ResultSet rs) throws SQLException {
        String username  = rs.getString("username");
        String authToken = rs.getString("authToken");
        return new AuthData(username, authToken);
    }
    @Override
    public void createAuth(AuthData auth)throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "INSERT INTO auth (username,authToken) VALUES (?,?)";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1,auth.username());
                ps.setString(2,auth.authToken());
                ps.executeUpdate();
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to create Auth", ex);
        }
    }
    @Override
    public void deleteAuth(String authToken)throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "DELETE FROM auth WHERE authToken = ?";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to delete Auth", ex);
        }
    }
    @Override
    public void clear()throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "DELETE FROM auth";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.executeUpdate();
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to clear Auth", ex);
        }
    }
}
