package server;

import dataaccess.MemoryUserDataAccess;
import dataaccess.UserDataAccess;

public class Service1 {
    UserDataAccess UserData;
    public Service1(){
        UserData = new MemoryUserDataAccess();
    }
    public boolean register(User user){
        if(user.username == null ||user.password == null){

        }else if(){

        }
        else{
            createUser();
            createAuth();
        }
    }
}
