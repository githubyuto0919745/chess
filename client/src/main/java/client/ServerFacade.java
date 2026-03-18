package client;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass){
        var status = response.statusCode();
        if(!isSuccessful(status)){
            var body = response.body();
            if(body != null){
                throw ResponseException.fromJson(body);
            }
            throw new ResponseException(ResponseException.fromHttpStatusCode(status),"other failure: " + status);
        }
    }
    private HttpResponse<String> sendRequest (HttpRequest request) throws {
        try{
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception ex){
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }


}
