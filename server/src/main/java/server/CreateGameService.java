package server;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.memory.AuthDAO;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import record.*;
import server.exceptions.BadRequestException;
import server.exceptions.UnauthorizedException;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class CreateGameService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public CreateGameService() throws DataAccessException {
        try {
            userDataAccess = new MySqlUserDAO();
            authDataAccess = new MySqlAuthDAO();
            gameDataAccess = new MySqlGameDAO();
        }catch( SQLException e){
            throw new DataAccessException("error",e);
        }
    }


    public GameData createGames(GameData game, String authToken) throws DataAccessException {
        AuthData auth = authDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new UnauthorizedException();
        }
        if(game == null || game.gameName() == null){
            throw new BadRequestException();
        }
        GameData newGame = new GameData(
                0,
                null,
                null,
                game.gameName(),
                new ChessGame()
        );
      return gameDataAccess.createGame(newGame);
    }
}