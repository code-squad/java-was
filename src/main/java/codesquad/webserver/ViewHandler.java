package codesquad.webserver;

import codesquad.model.Header;
import codesquad.model.responses.*;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class ViewHandler {
    private static final Logger log = getLogger(ViewHandler.class);
    private static final Map<ResponseCode, ResponseTemplate> templates = new HashMap<>();

    static {
        templates.put(ResponseCode.OK, new ResponseTemplate200());
        templates.put(ResponseCode.FOUND, new ResponseTemplate300());
    }

    public static void resolve(OutputStream out, Response response) throws Exception {
        DataOutputStream dos = new DataOutputStream(out);
        ResponseTemplate responseTemplate = response.chooseTemplate(templates);
        responseTemplate.header(dos, response);
        responseTemplate.body(dos, response);
    }
}
