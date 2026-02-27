package dataaccess;

import java.util.HashMap;
import Record.AuthData;

public class AuthDAO implements AuthDataAccess {
    HashMap<String, AuthData> auths = new HashMap<>();
    public AuthDAO() {
    }

    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }
    public AuthData createAuth(AuthData auth){
        return auths.put(auth.authToken(), auth);
    }
    public AuthData deleteAuth(String authToken){
        return auths.remove(authToken);
    }

}
