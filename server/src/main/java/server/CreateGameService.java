package server;

import chess.ChessGame;
import dataaccess.*;
import Record.*;
import server.exceptions.BadRequestException;
import server.exceptions.UnauthorizedException;

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