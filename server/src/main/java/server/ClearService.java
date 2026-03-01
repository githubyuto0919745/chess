package server;

import dataaccess.*;
import Record.*;

public class ClearService {

    public void clears() {
        new UserDAO().clear();
        new AuthDAO().clear();
        new GameDAO().clear();
    }
}