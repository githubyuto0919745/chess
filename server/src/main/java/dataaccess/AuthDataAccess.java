package dataaccess;
import record.AuthData;

import javax.xml.crypto.Data;

public interface AuthDataAccess {

   AuthData getAuth(String authToken) throws DataAccessException;
   void createAuth(AuthData auth)throws DataAccessException;
   void deleteAuth(String authToken)throws DataAccessException;
   void clear()throws DataAccessException;
}
