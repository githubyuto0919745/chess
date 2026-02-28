package dataaccess;
import Record.AuthData;
public interface AuthDataAccess {

   AuthData getAuth(String authToken);
   void createAuth(AuthData auth);
   void deleteAuth(String authToken);
   void clear();
}
