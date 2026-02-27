package dataaccess;

import java.util.HashMap;


public class UserDAO implements UserDataAccess{
    HashMap<String, UserData> users = new HashMap<>();
    public UserDAO() {

    }

    private UserData getUser(String username){
        return users.get(username);
    }
    private UserData createUser(UserData user){
        return users.put(user.getUsername(), user);
    }
    private UserData deleteUser(String username){
        return users.remove(username);
    }
}
