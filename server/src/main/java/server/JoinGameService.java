package server;

import dataaccess.*;
import dataaccess.memory.AuthDAO;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import record.*;
import server.exceptions.AlreadyTakenException;
import server.exceptions.BadRequestException;
import server.exceptions.UnauthorizedException;

public class JoinGameService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public JoinGameService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }


    public void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
        AuthData auth = authDataAccess.getAuth(authToken);
        if(auth ==null){
            throw new UnauthorizedException();
        }

        GameData game = gameDataAccess.getGame(gameID);
        if(game == null){
            throw new BadRequestException();
        }
        if(playerColor == null){
            throw new BadRequestException();
        }

        GameData updatedGame;

        if("WHITE".equals(playerColor)) {
            if(game.whiteUsername()!= null){
                throw new AlreadyTakenException();
            }
            updatedGame = new GameData(
                    game.gameID(),
                    auth.username(),
                    game.blackUsername(),
                    game.gameName(),
                    game.game()
            );
        }else if("BLACK".equals(playerColor)){
            if(game.blackUsername()!=null){
                throw new AlreadyTakenException();
            }
            updatedGame = new GameData(
                game.gameID(),
                game.whiteUsername(),
                auth.username(),
                game.gameName(),
                game.game()
                );
        }
        else{
            throw new BadRequestException();
        }
        gameDataAccess.updateGame(updatedGame);
    }
}
