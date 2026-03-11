package server;

import dataaccess.memory.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.memory.UserDAO;
import dataaccess.UserDataAccess;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlUserDAO;
import org.mindrot.jbcrypt.BCrypt;
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
        return BCrypt.checkpw(passwordRequest, user.password());
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
