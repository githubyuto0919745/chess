package dataaccess;

import java.util.HashMap;
import Record.AuthData;

public class AuthDAO implements AuthDataAccess {
    public static HashMap<String, AuthData> auths = new HashMap<>();
    public AuthDAO() {
    }

    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }
    public void createAuth(AuthData auth){
        auths.put(auth.authToken(), auth);
    }
    public void deleteAuth(String authToken){
        auths.remove(authToken);
    }
    public void clear(){
        auths.clear();
    }
}
