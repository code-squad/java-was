package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

public class StatusLine {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private final HttpStatus status;

    public StatusLine(HttpStatus status) {
        this.status = status;
    }

    public void writeStatusLine(DataOutputStream dos) throws IOException {
        dos.writeBytes(toString());
    }

    public boolean isRedirect() {
        return status == HttpStatus.FOUND;
    }

    @Override
    public String toString() {
        return HTTP_VERSION + " " + status;
    }
}
