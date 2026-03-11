package dataaccess;
import record.AuthData;
public interface AuthDataAccess {

   AuthData getAuth(String authToken) throws DataAccessException;
   void createAuth(AuthData auth);
   void deleteAuth(String authToken);
   void clear();
}
