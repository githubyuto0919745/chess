package server;

import dataaccess.*;
import record.*;
import server.exceptions.UnauthorizedException;

import java.util.Collection;

public class ListGamesService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public ListGamesService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }


    public Collection<GameData> listGames(String authToken){
        AuthData auth = authDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new UnauthorizedException();
        }
        return gameDataAccess.listGame();
    }
}

