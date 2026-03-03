package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import record.*;
import server.exceptions.UnauthorizedException;
import java.util.UUID;

public class LoginService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    public LoginService(){
        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
    }

    public static String generateToken(){
        return UUID.randomUUID().toString();
    }
    public boolean isValidPassword(UserData user, String passwordRequest){
        return user.password().equals(passwordRequest);
    }
    public AuthData login(String username, String password){
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
