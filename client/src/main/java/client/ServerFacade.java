package client;

import com.google.gson.Gson;
import record.AuthData;
import record.GameData;
import record.JoinGameRequest;
import record.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ServerFacade {
    private static final HttpClient client = HttpClient.newHttpClient();
    private final String severalUrl;
    public ServerFacade(String url){
        severalUrl = url;
    }


    public void clear () throws ResponseException{
        var request = buildRequest ("DELETE", "/db", null, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }
    public AuthData register(UserData user) throws ResponseException{
        var request = buildRequest ("POST", "/user", user, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }
    public AuthData login (UserData user) throws ResponseException{
        var request = buildRequest ("POST", "/session", user, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }
    public String logout (String authToken) throws ResponseException{
        var request = buildRequest ("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        return handleResponse(response, null);
    }
    public List<GameData> listGame(String authToken) throws ResponseException{
        var request = buildRequest ("GET", "/game", null, authToken);
        var response = sendRequest(request);

        GameData[] games = handleResponse(response, GameData[].class);

        List<GameData> list = new ArrayList<>(Arrays.asList(games));
        return list;
    }
    public GameData createGame (GameData game, String authToken) throws ResponseException{
        var request = buildRequest ("POST", "/game", game, authToken);
        var response = sendRequest(request);
        return handleResponse(response, GameData.class);
    }
    public GameData joinGame (JoinGameRequest game, String authToken) throws ResponseException{
        var request = buildRequest ("PUT", "/game", game, authToken);
        var response = sendRequest(request);
        return handleResponse(response, GameData.class);
    }


    private HttpRequest buildRequest(String method, String path, Object body, String authToken){
        var request = HttpRequest.newBuilder()
                .uri(URI.create(severalUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }
    private static HttpRequest.BodyPublisher makeRequestBody(Object request){
        if(request != null){
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }
    private static <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        int status = response.statusCode();
        if(!isSuccessful(status)){
            var body = response.body();
            if(body != null){
                throw ResponseException.fromJson(body);
            }
            throw new ResponseException(ResponseException.fromHttpStatusCode(status));
        }

        if(responseClass != null){
            return new Gson().fromJson(response.body(),responseClass);
        }
        return null;
    }
    private static HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException{
        try{
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    private static boolean isSuccessful(int status){
        return status / 100 == 2;
    }


}
