package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import Record.UserData;
import Record.AuthData;
import server.Exceptions.AlreadyTakenException;
import server.Exceptions.BadRequestException;

import java.util.UUID;

public class RegisterService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    public RegisterService(){
        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
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
            userDataAccess.createUser(user);
            authDataAccess.createAuth(auth);
            return auth;
        }

    }
}
