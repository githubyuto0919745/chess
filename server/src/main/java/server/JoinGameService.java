package server;

import dataaccess.*;
import dataaccess.memory.AuthDAO;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import record.*;
import server.exceptions.AlreadyTakenException;
import server.exceptions.BadRequestException;
import server.exceptions.UnauthorizedException;

import java.sql.SQLException;

public class JoinGameService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public JoinGameService() throws DataAccessException {
        try {
            userDataAccess = new MySqlUserDAO();
            authDataAccess = new MySqlAuthDAO();
            gameDataAccess = new MySqlGameDAO();
        }catch(SQLException e){
           throw new DataAccessException("error",e);
        }
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
