package util;

import java.util.*;

public class RequestUtils {

    public static List<String> splitRequestLine(String requestLine) {
        return Arrays.asList(requestLine.split(" "));
    }

    public static List<String> splitPathAndParams(String pathAndParams) {
        return Arrays.asList(pathAndParams.split("\\?"));
    }

    public static List<Pair> splitParams(String params) {
        List<Pair> pairs = new ArrayList<>();
        String[] paramsToken = params.split("&");
        for (String param : paramsToken) {
            String[] token = param.split("=");
            pairs.add(new Pair(token[0], token[1]));
        }
        return pairs;
    }

    public static Optional<Pair> splitHeader(String header) {
        String[] token = header.split(": ");
        if (token.length != 2) {
            return Optional.empty();
        }
        return Optional.of(new Pair(token[0], token[1]));
    }

    public static class Pair {
        final String key;
        final String value;

        Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public boolean isMatch(String key) {
            return this.key.equals(key);
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
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
