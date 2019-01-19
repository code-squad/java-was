package codesquad.webserver;

import codesquad.model.Request;
import codesquad.model.Url;
import codesquad.util.HttpRequestUtils;
import codesquad.util.IOUtils;
import codesquad.util.responses.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final String EMPTY = "";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            Request request = new Request();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            Url url = Url.of(line);

            int contentLength = 0;
            while (!EMPTY.equals(line)) {
                line = br.readLine();
                if (line.contains("Content-Length")) {
                    contentLength = Integer.parseInt(HttpRequestUtils.parseHeader(line).getValue());
                    request.setContentLength(contentLength);
                }
                log.debug(line);
            }

            ViewHandler viewHandler = new ViewHandler(out);

            url.setQueryValue(IOUtils.readData(br, contentLength));
            request.setUrl(url);
            if(MappingHandler.hasMappingPath(url)) {
                MappingHandler.invoke(request);
            }

            log.debug(request.toString());
            viewHandler.resolve(request);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
