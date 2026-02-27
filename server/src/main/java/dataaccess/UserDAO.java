package dataaccess;

import java.util.HashMap;
import Record.UserData;

public class UserDAO implements UserDataAccess{
    HashMap<String, UserData> users = new HashMap<>();
    public UserDAO() {

    }

    public UserData getUser(String username){
        return users.get(username);
    }
    public UserData createUser(UserData user){
        return users.put(user.username(), user);
    }
    public UserData deleteUser(String username){
        return users.remove(username);
    }

}
