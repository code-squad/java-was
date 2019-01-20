package codesquad.webserver;

import codesquad.model.Header;
import codesquad.model.responses.Response;
import codesquad.model.responses.Response200;
import codesquad.model.responses.Response300;
import codesquad.model.responses.ResponseCode;
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

    public void resolve(Header header) {
        Response response = header.getResponse(responses);
        response.header(dos, header);
        response.body(dos);
    }
}
