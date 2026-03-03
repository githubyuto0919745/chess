package dataaccess;
import record.UserData;

public interface UserDataAccess {

    UserData getUser(String username);
    void createUser(UserData user);
    void clear();
}
