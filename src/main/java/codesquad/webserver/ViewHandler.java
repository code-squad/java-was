package codesquad.webserver;

import codesquad.model.Request;
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

    public void resolve(Request request) {
        Response response = responses.get(request.getResponseCode());
        response.header(dos, request);
        response.body(dos);
    }
}
