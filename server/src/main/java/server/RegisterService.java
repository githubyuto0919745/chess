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

    public void Register(UserData user, AuthData auth){
        // username already exist
        if(user.username() == null && user.password() == null && user.email()==null) {
            throw new RuntimeException("Already Taken Exception");
        }else{
            userDataAccess.createUser(user);
            authDataAccess.createAuth(auth);

        }
    }
}
