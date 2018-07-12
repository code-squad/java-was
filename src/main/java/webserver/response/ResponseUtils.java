package webserver.response;

import webserver.HttpStatus;

import java.util.Map.Entry;

class ResponseUtils {

    static String formatResponseLine(String httpVersion, HttpStatus status) {
        return String.format("%s %s %s \r\n", httpVersion, status.getStatusCode(), status.name());
    }

    static String formatResponseHeader(Entry<String, String> header) {
        return String.format("%s : %s \r\n", header.getKey(), header.getValue());
    }
}
