package server;

import dataaccess.*;
import Record.*;

public class CreateGamesService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public CreateGamesService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }


    public GameData createGame(String authToken){
        AuthData auth = AuthDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new RuntimeException("Unauthorized");
        }
        GameDataAccess.createGame();

    }
}