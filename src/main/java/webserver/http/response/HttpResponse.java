package webserver.http.response;

import java.io.DataOutputStream;

public class HttpResponse {
    private DataOutputStream response;

    public HttpResponse() {
    }

    public HttpResponse(DataOutputStream response) {
        this.response = response;
    }

    public DataOutputStream getResponse() {
        return this.response;
    }
}
