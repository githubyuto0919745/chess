package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import Record.*;

public class LoginService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    public LoginService(){
        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
    }

    public boolean isValidPassword(UserData user, String passwordRequest){
        return user.password().equals(passwordRequest);
    }
    public void login(String username, String password, AuthData auth){
        UserData user = userDataAccess.getUser(username);

        if(user == null){
            throw new RuntimeException("Not Found Username");
        }
        if(!isValidPassword(user,password )){
            throw new RuntimeException("Password is invalid");
        }
        authDataAccess.createAuth(auth);

    }
}
