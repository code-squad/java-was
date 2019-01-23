package codesquad.webserver;

import codesquad.model.Header;
import codesquad.model.Url;
import codesquad.model.responses.Response;
import codesquad.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            Url url = Url.of(line);

            Map<String, String> headers = new HashMap<>();
            while (!EMPTY.equals(line = br.readLine())) {
                HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                headers.put(pair.getKey(), pair.getValue());
                log.debug(line);
            }
            log.debug(url.toString());

            Header header = new Header(url, headers);
            header.setBodyValue(br);

            if(MappingHandler.hasMappingPath(url)) {
                MappingHandler.invoke(header);
            }

            log.debug(header.toString());
            ViewHandler.resolve(out, header.toResponse());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
