package server;

import chess.ChessGame;
import dataaccess.*;
import Record.*;
import server.Exceptions.BadRequestException;
import server.Exceptions.UnauthorizedException;

public class CreateGameService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public CreateGameService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }


    public GameData createGames(GameData game, String authToken){
        AuthData auth = authDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new UnauthorizedException();
        }
        if(game == null || game.gameName() == null){
            throw new BadRequestException();
        }

        int newId = gameDataAccess.listGame().size() + 1;

        GameData newGame = new GameData(
                newId,
                null,
                null,
                game.gameName(),
                new ChessGame()
        );

      gameDataAccess.createGame(newGame);
      return newGame;

    }
}