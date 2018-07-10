package webserver;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {

    String httpMethod;
    String path;

    public Request(BufferedReader br) throws IOException {

        String line = br.readLine();
        httpMethod = line.split(" ")[0];
        path = line.split(" ")[1];
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }
}
