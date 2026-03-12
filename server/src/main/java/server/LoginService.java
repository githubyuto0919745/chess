package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.memory.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import dataaccess.UserDataAccess;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import org.mindrot.jbcrypt.BCrypt;
import record.*;
import server.exceptions.UnauthorizedException;

import java.sql.SQLException;
import java.util.UUID;

public class LoginService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    public LoginService() throws DataAccessException {
        try {
            userDataAccess = new MySqlUserDAO();
            authDataAccess = new MySqlAuthDAO();
        }catch(SQLException e){
            throw new DataAccessException("error",e);
        }
    }


    public static String generateToken(){
        return UUID.randomUUID().toString();
    }
    public boolean isValidPassword(UserData user, String passwordRequest){
        return BCrypt.checkpw(passwordRequest, user.password());
    }
    public AuthData login(String username, String password) throws DataAccessException {
        UserData user = userDataAccess.getUser(username);

        if(user == null ){
            throw new UnauthorizedException();
        }
        if(!isValidPassword(user,password)){
            throw new UnauthorizedException();
        }
        AuthData auth = new AuthData(user.username(),generateToken());
        authDataAccess.createAuth(auth);
        return auth;
    }
}
