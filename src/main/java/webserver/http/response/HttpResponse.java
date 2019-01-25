package webserver.http.response;

import java.io.DataOutputStream;
import java.io.IOException;

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

    public void writeHeader(String header) throws IOException {
        this.response.writeBytes(header);
    }

    public void writeBody(byte[] body) throws IOException {
        this.response.write(body, 0, body.length);
    }

    public void send() throws IOException {
        this.response.flush();
    }
}
