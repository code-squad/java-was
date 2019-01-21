package codesquad.webserver;

import codesquad.model.Header;
import codesquad.model.responses.ResponseTemplate;
import codesquad.model.responses.ResponseTemplate200;
import codesquad.model.responses.ResponseTemplate300;
import codesquad.model.responses.ResponseCode;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class ViewHandler {
    private static final Logger log = getLogger(ViewHandler.class);
    private static final Map<ResponseCode, ResponseTemplate> responses = new HashMap<>();

    static {
        responses.put(ResponseCode.OK, new ResponseTemplate200());
        responses.put(ResponseCode.FOUND, new ResponseTemplate300());
    }

    public static void resolve(OutputStream out, Header header) throws Exception {
        DataOutputStream dos = new DataOutputStream(out);
        ResponseTemplate responseTemplate = header.getResponse(responses);
        byte[] body = responseTemplate.header(dos, header);
        responseTemplate.body(dos, body);
    }
}
