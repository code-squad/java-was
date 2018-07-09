package webserver;

import java.io.OutputStream;

public class Resolver {

    public void resolve(Request request, OutputStream out) {
        String path = request.getPath();
        String httpMethod = request.getHttpMethod();

    }
}
