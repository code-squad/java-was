package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseBody {
    private final Resource resource;

    public ResponseBody(Resource resource) {
        this.resource = resource;
    }

    public void writeBody(DataOutputStream dos) throws IOException {
        if (resource == null) {
            throw new IOException();
        }
        byte[] body = resource.getBytes();
        dos.write(body, 0, body.length);
    }

    public boolean exists() {
        return resource.isEmpty();
    }

    public int getContentLength() {
        return resource.getLength();
    }
}
