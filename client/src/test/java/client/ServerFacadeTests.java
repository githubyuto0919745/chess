package client;

import org.junit.jupiter.api.*;
import record.GameData;
import record.JoinGameRequest;
import record.UserData;
import server.JoinGameService;
import server.Server;

import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + port;
        facade = new ServerFacade(url);

    }
    @BeforeEach
    void clearDB() throws Exception{
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }



    @Test
    @DisplayName("register positive")
    public void registerPositive() throws Exception {
        var authData = facade.register(new UserData("player1","password1","p1@gmail.com"));
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    @DisplayName("register negative")
    public void registerNegative() throws Exception {
        facade.register(new UserData("player1","password1","p1@gmail.com"));
        boolean fail = false;
        try{
            facade.register(new UserData("player1","password2","p2@gmail.com"));
        }catch(ResponseException ex){
            fail = true;
        }
        Assertions.assertTrue(fail);
    }

    @Test
    @DisplayName("login positive")
    public void loginPositive() throws Exception {
        facade.register(new UserData("player1","password1","p1@gmail.com"));

        var authData = facade.login(new UserData("player1","password1","p1@gmail.com"));
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    @DisplayName("login negative")
    public void loginNegative() throws Exception {
        facade.register(new UserData("player1","password","p1@gmail.com"));

        boolean fail = false;
        try{
            facade.register(new UserData("player1","password1","p1@gmail.com"));
        }catch(ResponseException ex){
            fail = true;
        }
        Assertions.assertTrue(fail);
    }

    @Test
    @DisplayName("logout positive")
    public void logoutPositive() throws Exception {
        facade.register(new UserData("player1","password1","p1@gmail.com"));
        var loginAuth = facade.login(new UserData("player1","password1","p1@gmail.com"));

        String logoutAuth = facade.logout(loginAuth.authToken());
        Assertions.assertNull(logoutAuth);
    }

    @Test
    @DisplayName("logout negative")
    public void logoutNegative() throws Exception {
        boolean fail = false;
        try{
            facade.logout("tokenInvalid");
        }catch(ResponseException ex){
            fail = true;
        }
        Assertions.assertTrue(fail);
    }

    @Test
    @DisplayName("createGame positive")
    public void createGamePositive() throws Exception {
        var auth = facade.register(new UserData("player1","password1","p1@gmail.com"));

        var newGame = new GameData(null,"player1",null,"newGame",null);
        var createdGame = facade.createGame(newGame, auth.authToken());

        Assertions.assertNotNull(createdGame.gameID());
        Assertions.assertEquals(null, createdGame.whiteUsername());
    }

    @Test
    @DisplayName("createGame negative")
    public void createGameNegative() throws Exception {
        boolean fail = false;
        try{
            var newGame = new GameData(null,"whitePlayer",null,"InvalidGame",null);
            facade.createGame(newGame, "tokenInvalid");
        }catch(ResponseException ex){
            fail = true;
        }
        Assertions.assertTrue(fail);
    }


    @Test
    @DisplayName("listGame positive")
    public void listGamePositive() throws Exception {
        var auth = facade.register(new UserData("player1","password1","p1@gmail.com"));

        var game1 = new GameData(null,null,null,"game1",null);
        facade.createGame(game1, auth.authToken());

        List<GameData> listedGame = facade.listGame(auth.authToken());
        Assertions.assertEquals(null, listedGame.getFirst().whiteUsername());


    }

    @Test
    @DisplayName("listGame negative")
    public void listGameNegative() throws Exception {
        boolean fail = false;
        try{
            facade.listGame("tokenInvalid");
        }catch(ResponseException ex){
            fail = true;
        }
        Assertions.assertTrue(fail);
    }

    @Test
    @DisplayName("joinGame positive")
    public void joinGamePositive() throws Exception {
        var whiteAuth = facade.register(new UserData("player1","password1","p1@gmail.com"));
        var blackAuth = facade.register(new UserData("player2","password2","p2@gmail.com"));

        var newGame = new GameData(null,"player1",null,"game1",null);
        var createdGame = facade.createGame(newGame, whiteAuth.authToken());

        JoinGameRequest join = new JoinGameRequest(createdGame.gameID(),"BLACK");
        GameData joinedGame = facade.joinGame(join,blackAuth.authToken());

        Assertions.assertNotNull(join.gameID());
        Assertions.assertEquals("player1", joinedGame.whiteUsername());
        Assertions.assertEquals("player2", joinedGame.blackUsername());
    }

    @Test
    @DisplayName("joinGame negative")
    public void joinGameNegative() throws Exception {
        boolean fail = false;
        try{
            facade.listGame("tokenInvalid");
        }catch(ResponseException ex){
            fail = true;
        }
        Assertions.assertTrue(fail);
    }
}
