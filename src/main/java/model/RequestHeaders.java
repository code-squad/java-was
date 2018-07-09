package model;

import com.google.common.base.Strings;
import exception.HeaderNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class RequestHeaders {
    private static final String SPLIT_REGEX = ": ";

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
        return headers.stream().filter(header -> header.isMatch(key)).findFirst().map(Pair::get).orElseThrow(HeaderNotFoundException::new);
    }

    private Pair parseHeader(String headerSource) {
        if (Strings.isNullOrEmpty(headerSource)) {
            return null;
        }

        String[] token = headerSource.split(SPLIT_REGEX);
        if (token.length != 2) {
            return null;
        }
        return new Pair(token[0], token[1]);
    }

    private static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        boolean isMatch(String key) {
            return this.key.equals(key);
        }

        String get() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Objects.equals(key, pair.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}
