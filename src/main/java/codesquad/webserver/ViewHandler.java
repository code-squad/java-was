package codesquad.webserver;

import codesquad.model.Url;
import codesquad.util.responses.Response;
import codesquad.util.responses.Response200;
import codesquad.util.responses.Response300;
import codesquad.util.responses.ResponseCode;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class ViewHandler {
    private static final Logger log = getLogger(ViewHandler.class);
    private static final Map<ResponseCode, Response> responses = new HashMap<>();
    private DataOutputStream dos;

    static {
        responses.put(ResponseCode.OK, new Response200());
        responses.put(ResponseCode.FOUND, new Response300());
    }

    public ViewHandler(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void resolve(Url url, ResponseCode responseCode) {
        log.debug("responseCode : {}", responseCode);
        log.debug("url : {}", url);

        Response response = responses.get(responseCode);
        response.header(dos, url);
        response.body(dos);
    }
}
