package dataaccess;

import chess.ChessGame;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import record.AuthData;
import record.GameData;
import record.UserData;

import java.util.Collection;

public class DAOTests {
    private MySqlUserDAO userDAO;
    private MySqlAuthDAO authDAO;
    private MySqlGameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException{
        userDAO = new MySqlUserDAO();
        authDAO = new MySqlAuthDAO();
        gameDAO = new MySqlGameDAO();

        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

    }
    @Test
    @DisplayName("create User Success")
    public void createUserSuccess() throws DataAccessException {
        UserData user = new UserData("user1", "123456", "yuto@gmail.com");
        userDAO.createUser(user);

        UserData check = userDAO.getUser("user1");
        Assertions.assertNotNull(check);
        Assertions.assertEquals("user1", check.username());
    }
    @Test
    @DisplayName("create User Failure")
    public void createUserFailure(){
        new UserData("", "", "yuto1@gmail.com");
        Assertions.assertFalse(false);
    }

    @Test
    @DisplayName("create Auth Success")
    public void createAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("user1", "token1");
        authDAO.createAuth(auth);
        AuthData check = authDAO.getAuth("token1");
        Assertions.assertNotNull(check);
    }
    @Test
    @DisplayName("create Auth Failure")
    public void createAuthFailure(){
        new AuthData("", "token2");
        Assertions.assertFalse(false);
    }

    @Test
    @DisplayName("create Game Success")
    public void createGameSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("whiteUser","2345", "white@gmail.com"));
        userDAO.createUser(new UserData("blackUser", "1234", "black@gmail.com"));
        GameData game = new GameData(null,  "whiteUser", "blackUser","gameTest", new ChessGame());
        GameData check = gameDAO.getGame(game.gameID());
        Assertions.assertAll(
                () -> Assertions.assertNotNull(check),
                () -> Assertions.assertEquals("whiteUser", check.whiteUsername()),
                () -> Assertions.assertEquals("blackUser", check.blackUsername()),
                () -> Assertions.assertEquals("gameTest", check.gameName())
        );
    }
    @Test
    @DisplayName("create Game Failure")
    public void createGameFailure() {
        try{
            GameData game = new GameData(null, "", "",null,null);
            gameDAO.createGame(game);
            Assertions.fail("Failed create Game");
        }catch(DataAccessException e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @DisplayName("get User Success")
    public void getUserSuccess() throws DataAccessException {
        UserData user = new UserData("user2", "123456", "yuto2@gmail.com");
        userDAO.createUser(user);
        UserData check = userDAO.getUser("user2");
        Assertions.assertNotNull(check);
        Assertions.assertEquals("user2",check.username());
        Assertions.assertEquals("123456",check.password());
        Assertions.assertEquals("yuto2@gmail.com",check.email());
    }
    @Test
    @DisplayName("get User Failure")
    public void getUserFailure() {
        new UserData("", "", "yuto2@gmail.com");
        Assertions.assertFalse(false);
    }

    @Test
    @DisplayName("get Auth Success")
    public void getAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("user2", "token2");
        authDAO.createAuth(auth);
        AuthData check = authDAO.getAuth("token2");
        Assertions.assertNotNull(check);
        Assertions.assertEquals("user2",check.username());
        Assertions.assertEquals("token2",check.authToken());
    }
    @Test
    @DisplayName("get Auth Failure")
    public void getAuthFailure(){
        new AuthData("", "token2");
        Assertions.assertFalse(false);
    }

    @Test
    @DisplayName("get Game Success")
    public void getGameSuccess() throws DataAccessException {

        userDAO.createUser(new UserData("whiteUser","2345", "white@gmail.com"));
        userDAO.createUser(new UserData("blackUser", "1234", "black@gmail.com"));

        GameData game = new GameData(null,  "whiteUser", "blackUser","gameTest", new ChessGame());
        game = gameDAO.createGame(game);

        GameData check = gameDAO.getGame(game.gameID());

        Assertions.assertNotNull(check);
        Assertions.assertEquals("whiteUser",check.whiteUsername());
        Assertions.assertEquals("blackUser",check.blackUsername());
        Assertions.assertEquals("gameTest",check.gameName());
    }


    @Test
    @DisplayName("get Game Failure")
    public void getGameFailure() throws DataAccessException {
        GameData game = gameDAO.getGame(-1);
        Assertions.assertNull(game, "Failed get Game");
    }

    @Test
    @DisplayName("delete Auth Success")
    public void deleteAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("user2", "token2");
        authDAO.createAuth(auth);
        authDAO.deleteAuth("token2");
        AuthData deleted = authDAO.getAuth("token2");
        Assertions.assertNull(deleted, "Auth is deleted");
    }
    @Test
    @DisplayName("delete Auth Failure")
    public void deleteAuthFailure(){
        try{
            authDAO.getAuth("not found");
            Assertions.assertFalse(false);
        }catch(DataAccessException e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @DisplayName("list Game Success")
    public void listGameSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("whiteUser", "2345", "white@gmail.com"));
        userDAO.createUser(new UserData("blackUser", "1234", "black@gmail.com"));

        gameDAO.createGame(new GameData(null, "whiteUser", "blackUser", "game1", null));
        gameDAO.createGame(new GameData(null, "whiteUser", "blackUser", "game2", null));

        Collection<GameData> games = gameDAO.listGame();
        Assertions.assertNotNull(games);
        Assertions.assertEquals(2, games.size());

    }
    @Test
    @DisplayName("list Game Failure")
    public void listGameFailure() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGame();
        Assertions.assertNotNull(games);
    }

    @Test
    @DisplayName("update Game Success")
    public void updateGameSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("whiteUser", "2345", "white@gmail.com"));
        userDAO.createUser(new UserData("blackUser", "1234", "black@gmail.com"));

        GameData game = new GameData(null,  "whiteUser", "blackUser", "updateGame", new ChessGame());
        game = gameDAO.createGame(game);

        GameData updateGame = new GameData(game.gameID(),  "whiteUser", "blackUser", "updateGame", new ChessGame());
        gameDAO.createGame(updateGame);


        GameData check = gameDAO.getGame(game.gameID());
        Assertions.assertNotNull(check);
        Assertions.assertEquals("updateGame", check.gameName());

    }
    @Test
    @DisplayName("update Game Failure")
    public void updateGameFailure() throws DataAccessException {
        try{
            GameData game = new GameData(1111,  "whiteUser", "blackUser", "gameTest", new ChessGame());;
            gameDAO.updateGame(game);
            Assertions.assertFalse(false);
        }catch(DataAccessException e){
            Assertions.assertTrue(true);
        }

    }

    @Test
    @DisplayName("Clear Success")
    public void setClearSuccess() throws DataAccessException {
        new UserData("user1", "123456", "yuto@gmail.com");
        userDAO.clear();
        new AuthData("user1", "token1");
        authDAO.clear();
        new GameData(null,  "whiteUser", "blackUser", "updateGame", new ChessGame());
        gameDAO.clear();
        Assertions.assertTrue(true);
    }

}
