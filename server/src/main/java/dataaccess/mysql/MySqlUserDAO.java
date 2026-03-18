package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDataAccess;
import record.GameData;
import record.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserDAO implements UserDataAccess {

    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }
    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS user (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `username` VARCHAR(256) NOT NULL UNIQUE,
            `password` VARCHAR(256) NOT NULL,
            `email` VARCHAR(256) NOT NULL
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
            throw new DataAccessException("Unable to configure table",ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "SELECT id, username, password, email FROM user WHERE username = ?";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1, username);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readUser(rs);
                    }
                }
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to getUser",ex);
        }
        return null;
    }
    private UserData readUser(ResultSet rs) throws SQLException {
        String username  = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");

        return new UserData(username, password, email);
    }
    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "INSERT INTO user (username, password, email) VALUES (?,?,?)";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.setString(1,user.username());
                ps.setString(2,user.password());
                ps.setString(3, user.email());
                ps.executeUpdate();
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to createUser", ex);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection connect = DatabaseManager.getConnection()){
            var table = "DELETE FROM user";
            try(PreparedStatement ps = connect.prepareStatement(table)){
                ps.executeUpdate();
            }
        }catch(SQLException ex){
            throw new DataAccessException("Unable to clear User",ex);
        }
    }
}
