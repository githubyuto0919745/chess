package dataaccess;

import java.util.HashMap;

public class AuthDAO {
    HashMap<String, AuthData> auths = new HashMap<>();
        public AuthDAO() {

        }

        private AuthData getAuth(String authToken) {
            return auths.get(authToken);
        }
        private AuthData createAuth(AuthData auth){
            return auths.put(auth.getAuthtoken(), auth);
        }
        private AuthData deleteUser(String authToken){
            return auths.remove(authToken);
        }

}
