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


    public void listGame(String authToken){
        AuthData auth = authDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new RuntimeException("Unauthorized");
        }
        gameDataAccess.listUser();
    }
}

