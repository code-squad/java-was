package webserver.response;

import webserver.HttpBody;
import webserver.HttpHeader;
import webserver.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static webserver.response.ResponseHeaderAttribute.LOCATION;
import static webserver.response.ResponseUtils.formatResponseHeader;
import static webserver.response.ResponseUtils.formatResponseLine;

public class Response {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String EOF = "\r\n";

    private HttpStatus status;
    private HttpHeader header;
    private HttpBody body;

    private DataOutputStream out;

    public Response(OutputStream out) {
        this.out = new DataOutputStream(out);
        header = new HttpHeader();
    }

    public Response setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public Response setHeader(ResponseHeaderAttribute attribute, String value) {
        header.add(attribute.name(), value);
        return this;
    }

    public Response setBody(String body) {
        this.body = new HttpBody(Optional.of(body));
        return this;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getPath() {
        return header.getHeader(LOCATION.name());
    }

    public void response() throws IOException {
        out.writeBytes(formatResponseLine(HTTP_VERSION, status));
        Map<String, String> headers = header.getHeaders();
        for (Entry<String, String> keyAndValue : headers.entrySet()) {
            out.writeBytes(formatResponseHeader(keyAndValue));
        }
        out.writeBytes(EOF);
        out.writeBytes(body.get());
        out.flush();
    }
}
