package server;

import dataaccess.*;
import Record.*;

public class JoinGameService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public JoinGameService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }


    public GameData JoinGame(String authToken, int gameID,String playerColor){
        AuthData auth = authDataAccess.getAuth(authToken);
        if(auth ==null){
            throw new RuntimeException("Unauthorized");
        }

        GameData game = gameDataAccess.getGame(gameID);
        if(game == null){
            throw new RuntimeException("Game NOT Found");
        }

        GameData updatedGame;
        if(playerColor.equals("WHITE")) {
            if(game.whiteUsername()!= null){
                throw new RuntimeException(" White is already taken");
            }
            updatedGame = new GameData(
                    game.gameID(),
                    game.blackUsername(),
                    auth.username(),
                    game.gameName(),
                    game.game()
            );
            gameDataAccess.updateGame(updatedGame);

        }else if(playerColor.equals("BLACK")){
            if(game.blackUsername()!=null){
                throw new RuntimeException("Black is already taken");
            }
            updatedGame = new GameData(
                game.gameID(),
                auth.username(),
                game.whiteUsername(),
                game.gameName(),
                game.game()
                );
            gameDataAccess.updateGame(updatedGame);
        }
        else{
            throw new RuntimeException("Invalid Color");
        }
        return updatedGame;
    }
}
