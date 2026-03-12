package server;

import dataaccess.DataAccessException;
import dataaccess.memory.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import dataaccess.UserDataAccess;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import org.mindrot.jbcrypt.BCrypt;
import record.UserData;
import record.AuthData;
import server.exceptions.AlreadyTakenException;
import server.exceptions.BadRequestException;

import java.sql.SQLException;
import java.util.UUID;

public class RegisterService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    public RegisterService() throws DataAccessException {
        userDataAccess = new MySqlUserDAO();
        authDataAccess = new MySqlAuthDAO();
    }
    public void storeUserPassword(String username, String clearPassword, String email) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(clearPassword, BCrypt.gensalt());
        UserData user = new UserData(username, hashedPassword,email);
        userDataAccess.createUser(user);
    }
    public static String generateToken(){
        return UUID.randomUUID().toString();
    }
    public AuthData register(UserData user) throws DataAccessException {
        // username already exist
        if(userDataAccess.getUser(user.username()) != null) {
            throw new AlreadyTakenException();
        }
        if(user.username() == null || user.username().trim().isEmpty() || user.password() == null || user.password().trim().isEmpty()) {
            throw new BadRequestException();
        }else{
            AuthData auth = new AuthData(user.username(), generateToken());
            storeUserPassword(user.username(), user.password(),user.email());
            authDataAccess.createAuth(auth);
            return auth;
        }

    }
}
