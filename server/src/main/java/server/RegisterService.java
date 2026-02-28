package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import Record.UserData;
import Record.AuthData;

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
            throw new RuntimeException("User already exists");
        }
        if(user.username() == null && user.password() == null) {
            throw new RuntimeException("Invalid Input");
        }else{
            userDataAccess.createUser(user);
            authDataAccess.createAuth(auth);
        }
    }
}
