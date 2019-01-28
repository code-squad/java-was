package webserver;

import vo.DispatchPath;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class Dispatcher {
    public static void dispatch(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        DispatchPath.findController(path).service(request, response);
    }
}
