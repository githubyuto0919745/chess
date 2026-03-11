package dataaccess.memory;

import java.util.HashMap;

import dataaccess.UserDataAccess;
import record.UserData;

public class UserDAO implements UserDataAccess {
    public static HashMap<String, UserData> users = new HashMap<>();
    public UserDAO() {

    }

    public UserData getUser(String username){
        return users.get(username);
    }
    public void createUser(UserData user){
        users.put(user.username(), user);
    }
    public void clear(){
        users.clear();
    }
}
