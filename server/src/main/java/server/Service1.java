package server;

import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import Record.UserData;

public class Service1 {
    UserDataAccess userDataAccess;
    public Service1(){
        userDataAccess = new UserDAO();
    }
    public boolean register(UserData user){
        if(user.username() == null ||user.password() == null){

        }else if(){

        }
        else{
            userDataAccess.createUser();
            createAuth();
        }
    }
}
