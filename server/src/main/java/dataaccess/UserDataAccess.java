package dataaccess;
import Record.UserData;

public interface UserDataAccess {

    UserData getUser(String username);
    UserData createUser(UserData user);
    UserData deleteUser(String username);
}
