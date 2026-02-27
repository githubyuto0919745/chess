package dataaccess;
import Record.AuthData;
public interface AuthDataAccess {

   AuthData getAuth(String authToken);
   AuthData createAuth(AuthData auth);
   AuthData deleteAuth(String authToken);

}
