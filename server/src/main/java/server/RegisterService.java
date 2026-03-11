package server;

import dataaccess.memory.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.memory.UserDAO;
import dataaccess.UserDataAccess;
import org.mindrot.jbcrypt.BCrypt;
import record.UserData;
import record.AuthData;
import server.exceptions.AlreadyTakenException;
import server.exceptions.BadRequestException;

import java.util.UUID;

public class RegisterService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    public RegisterService(){
        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
    }
    public void storeUserPassword(String username, String clearPassword, String email){
        String hashedPassword = BCrypt.hashpw(clearPassword, BCrypt.gensalt());
        UserData user = new UserData(username, hashedPassword,email);
        userDataAccess.createUser(user);
    }
    public static String generateToken(){
        return UUID.randomUUID().toString();
    }
    public AuthData register(UserData user){
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
