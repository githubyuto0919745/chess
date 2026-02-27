package server;

import dataaccess.*;
import Record.*;

public class JoinGamesService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public JoinGamesService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }


    public GameData joinGame(String authToken){
        AuthData auth = AuthDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new RuntimeException("Unauthorized");
        }
        GameDataAccess.updateGame();
    }
}
