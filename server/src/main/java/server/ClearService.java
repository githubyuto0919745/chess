package server;

import dataaccess.*;
import dataaccess.memory.AuthDAO;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;

public class ClearService {

    private final UserDataAccess userDataAccess = new UserDAO();
    private final AuthDataAccess authDataAccess = new AuthDAO();
    private final GameDataAccess gameDataAccess = new GameDAO();

    public void clears() {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }
}