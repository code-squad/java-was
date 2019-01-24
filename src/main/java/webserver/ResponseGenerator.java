package webserver;

import webserver.http.response.HttpResponse;

import java.io.DataOutputStream;

public class ResponseGenerator {
    public static HttpResponse generateResponse(DataOutputStream dos) {
        return new HttpResponse(dos);
    }
}
