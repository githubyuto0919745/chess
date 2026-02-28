package server;

import dataaccess.*;
import Record.*;

public class ClearService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;

    public ClearService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }

    public void Clear(UserData user, AuthData auth, GameData game){
        userDataAccess.deleteUser(user.username());
        authDataAccess.deleteAuth(auth.authToken());
        gameDataAccess.deleteGame(game.gameID());
    }
}