package dataaccess;

import server.User;

import java.util.ArrayList;

public class MemoryUserDataAccess implements UserDataAccess{
    ArrayList<User> users = new ArrayList<>();
    //hashmap
    public MemoryUserDataAccess() {

    }

    private boolean doesAccountExist(User user){
        for (int i = 0; i< users.size(); i ++){
            if (users.username == user.username){
                return false;
                break;
            }
            else{
                if(users.password == user.password){
                    return false;
                    break;
                }
            }
            return true;
            break;
        }
    }
}
