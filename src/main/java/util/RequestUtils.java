package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.stream.Collectors;

public class RequestUtils {

    public static List<String> splitRequestLine(String requestLine) {
        return Arrays.asList(requestLine.split(" "));
    }

    public static List<String> splitPathAndParams(String pathAndParams) {
        return Arrays.asList(pathAndParams.split("\\?"));
    }

    public static Map<String, String> splitQueryString(String queryString) {
        return splitValues(queryString, "&");
    }

    private static Map<String, String> splitValues(String target, String regex) {
        if (Strings.isNullOrEmpty(target)) {
            return Maps.newHashMap();
        }
        String[] token = target.split(regex);
        return Arrays.stream(token).map(t -> getKeyValue(t, "=")).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public static Optional<Pair> splitHeader(String header) {
        return getKeyValue(header, ": ");
    }

    private static Optional<Pair> getKeyValue(String keyValue, String regex) {
        String[] token = keyValue.split(regex);
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
