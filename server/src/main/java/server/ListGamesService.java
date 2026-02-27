package server;

import dataaccess.*;
import Record.*;

public class ListGamesService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public ListGamesService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }


    public boolean ListGame(String authToken){
        AuthData auth = AuthDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new RuntimeException("Unauthorized");
        }
        GameDataAccess.listUser();
        return true;
    }
}

