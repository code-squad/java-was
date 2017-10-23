package requestmapping;

import util.HttpRequestUtils.RequestTypes;

public class RequestLineFactory {

    public static RequestLine generateRequestLine(RequestTypes type, String path) {
        return new RequestLine(path, type);
    }
}
