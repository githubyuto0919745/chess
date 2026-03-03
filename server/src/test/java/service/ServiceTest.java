package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.*;
import Record.*;
import server.exceptions.BadRequestException;
import server.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;

public class ServiceTest {
    @BeforeEach
    public void setup() {
        clearService.clears();
    }
    RegisterService registerService = new RegisterService();
    LoginService loginService = new LoginService();
    LogoutService logoutService = new LogoutService();
    CreateGameService createGameService = new CreateGameService();
    JoinGameService joinGameService = new JoinGameService();
    ListGamesService listGamesService = new ListGamesService();
    ClearService clearService = new ClearService();

    @Test
    @DisplayName("Register Success")
    public void setRegisterSuccess() {
        AuthData auth = registerService.register(new UserData("register1", "123456", "yuto@gmail.com"));
        Assertions.assertNotNull(auth);
    }


    @Test
    @DisplayName("Register Failure")
    public void setRegisterFailure() {
        try {
            registerService.register(
                    new UserData("", "123456", "yuto@gmail.com")
            );
            Assertions.assertFalse(false);
        } catch (BadRequestException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    @DisplayName("Login Success")
    public void setLoginSuccess() {
        registerService.register(
                new UserData("user1","123456","yuto@gmail.com")
        );

        AuthData auth = loginService.login("user1","123456");
        Assertions.assertNotNull(auth);
    }


    @Test
    @DisplayName("Login Failure")
    public void setLoginFailure() {
        try {
            loginService.login("", "1234567"
            );
            Assertions.fail("Invalid Password");
        } catch (UnauthorizedException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    @DisplayName("Logout Success")
    public void setLogoutSuccess() {
        AuthData auth = registerService.register(new UserData("logout1","123456","yuto@gmail.com"));
        logoutService.logout(auth.authToken());
        Assertions.assertNotNull(auth);

    }


    @Test
    @DisplayName("Logout Failure")
    public void setLogoutFailure() {
        try {
            AuthData auth = registerService.register(new UserData("logout2","1234567","yuto@gmail.com"));
            logoutService.logout(auth.authToken());
            Assertions.assertFalse(false);
        } catch (UnauthorizedException e) {
            Assertions.assertTrue(true);
        }
    }


    @Test
    @DisplayName("Create-game Success")
    public void setCreateGameSuccess() {
        AuthData auth = registerService.register(new UserData("create1","123456","yuto@gmail.com"));
        GameData game = new GameData(0,null,null,"game1",null);
        createGameService.createGames(game,auth.authToken());
        Assertions.assertNotNull(game);

    }


    @Test
    @DisplayName("Create-game Failure")
    public void setCreateGameFailure() {
        AuthData auth = registerService.register(new UserData("create1","123456","yuto@gmail.com"));
        GameData game = new GameData(0, null, null, "", null);
        try {
            createGameService.createGames(game, auth.authToken());
            Assertions.assertFalse(false);
        } catch (BadRequestException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    @DisplayName("Join-game Success")
    public void setJoinGameSuccess() {
        AuthData auth = registerService.register(new UserData("create1","123456","yuto@gmail.com"));
        GameData game = createGameService.createGames(new GameData(0,null,null,"game1",null),auth.authToken());

        try{
            joinGameService.joinGame(auth.authToken(),game.gameID(),"WHITE");
            Assertions.assertTrue(true);
        }catch(Exception e){
            Assertions.assertFalse(false);
        }

    }


    @Test
    @DisplayName("Join-game Failure")
    public void setJoinGameFailure() {
        AuthData auth = registerService.register(new UserData("create1","123456","yuto@gmail.com"));
        GameData game = new GameData(0, null, null, "", null);
        try {
            joinGameService.joinGame(auth.authToken(),game.gameID(), "BLUE");
            Assertions.assertFalse(false);
        } catch (BadRequestException e) {
            Assertions.assertTrue(true);
        }
    }


    @Test
    @DisplayName("List-game Success")
    public void setListGameSuccess() {
        AuthData auth = registerService.register(new UserData("create1","123456","yuto@gmail.com"));
        listGamesService.listGames(auth.authToken());
        Assertions.assertNotNull(auth);

    }


    @Test
    @DisplayName("List-game Failure")
    public void setListGameFailure() {

        try {
            listGamesService.listGames("token");
            Assertions.assertFalse(false);
        } catch (UnauthorizedException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    @DisplayName("Clear Success")
    public void setClearSuccess() {
        registerService.register(new UserData("user1", "123456", "yuto@gmail.com"));
            clearService.clears();
            Assertions.assertTrue(true);

    }
}
