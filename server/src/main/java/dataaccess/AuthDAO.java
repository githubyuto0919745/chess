package dataaccess;

import java.util.HashMap;
import Record.AuthData;

public class AuthDAO {
    HashMap<String, AuthData> auths = new HashMap<>();
    public AuthDAO() {
    }

    private AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }
    private AuthData createAuth(AuthData auth){
        return auths.put(auth.authToken(), auth);
    }
    private AuthData deleteAuth(String authToken){
        return auths.remove(authToken);
    }

}
