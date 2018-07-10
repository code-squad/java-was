package webserver;

import com.google.common.base.Strings;
import exception.HeaderNotFoundException;
import util.RequestUtils;
import util.RequestUtils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class RequestHeaders {
    private List<Pair> headers;

    RequestHeaders() {
        headers = new ArrayList<>();
    }

    RequestHeaders add(String headerSource) {
        Pair header = parseHeader(headerSource);
        if (!Objects.isNull(header) && headers.stream().noneMatch(pair -> pair.equals(header))) {
            headers.add(header);
        }
        return this;
    }

    String getHeader(String key) {
        return headers.stream().filter(header -> header.isMatch(key)).findFirst().map(Pair::getValue).orElseThrow(HeaderNotFoundException::new);
    }

    private Pair parseHeader(String headerSource) {
        if (Strings.isNullOrEmpty(headerSource)) {
            return null;
        }
        return RequestUtils.splitHeader(headerSource).orElse(null);
    }
}
