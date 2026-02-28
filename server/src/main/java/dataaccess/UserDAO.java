package dataaccess;

import java.util.HashMap;
import Record.UserData;

public class UserDAO implements UserDataAccess{
    private static final HashMap<String, UserData> users = new HashMap<>();
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
