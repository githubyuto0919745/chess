package server;

import dataaccess.*;
import Record.*;

public class CreateGameService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public CreateGameService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }


    public void CreateGame(GameData game, String authToken){
        AuthData auth = authDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new RuntimeException("Unauthorized");
        }
        gameDataAccess.createGame(game);

    }
}