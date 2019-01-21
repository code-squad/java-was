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

    static {
        responses.put(ResponseCode.OK, new Response200());
        responses.put(ResponseCode.FOUND, new Response300());
    }

    public static void resolve(OutputStream out, Header header) throws Exception {
        DataOutputStream dos = new DataOutputStream(out);
        Response response = header.getResponse(responses);
        byte[] body = response.header(dos, header);
        response.body(dos, body);
    }
}
