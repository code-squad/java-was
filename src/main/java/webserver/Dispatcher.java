package webserver;

import vo.DispatchPath;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public static void dispatch(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        DispatchPath.findController(path).service(request, response);
    }
}
