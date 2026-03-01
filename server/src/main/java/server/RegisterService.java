package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import Record.UserData;
import Record.AuthData;
import server.Exceptions.AlreadyTakenException;
import server.Exceptions.BadRequestException;

public class RegisterService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    public RegisterService(){
        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
    }

    public void register(UserData user, AuthData auth){
        // username already exist
        if(userDataAccess.getUser(user.username()) != null) {
            throw new AlreadyTakenException();
        }
        if(user.username() == null && user.password() == null) {
            throw new BadRequestException();
        }else{
            userDataAccess.createUser(user);
            authDataAccess.createAuth(auth);
        }
    }
}
