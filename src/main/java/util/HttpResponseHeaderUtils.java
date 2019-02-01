package util;

public class HttpResponseHeaderUtils {
    public static final String NEW_LINE = System.lineSeparator();

    public static String generate200Header(int lengthOfBodyContent) {
        StringBuilder sb = new StringBuilder()
                .append("HTTP/1.1 200 OK")
                .append(NEW_LINE)
                .append(generateHtmlContentType())
                .append(generateContentLength(lengthOfBodyContent))
                .append(NEW_LINE);
        return sb.toString();
    }

    public static String generate302Header(String path) {
        StringBuilder sb = new StringBuilder()
                .append("HTTP/1.1 302 Found")
                .append(NEW_LINE)
                .append("Location: ")
                .append(path)
                .append(NEW_LINE)
                .append(generateHtmlContentType())
                .append(NEW_LINE);
        return sb.toString();
    }

    public static String generate302CookieHeader(String path, boolean bool) {
        StringBuilder sb = new StringBuilder()
                .append("HTTP/1.1 302 Found")
                .append(NEW_LINE)
                .append("Location: ")
                .append(path)
                .append(NEW_LINE)
                .append(generateHtmlContentType())
                .append(generateCookie(bool))
                .append(NEW_LINE);
        return sb.toString();
    }

    private static String generateHtmlContentType() {
        StringBuilder sb = new StringBuilder()
                .append("Content-Type: text/html;charset=utf-8")
                .append(NEW_LINE);
        return sb.toString();
    }

    private static String generateContentLength(int lengthOfBodyContent) {
        StringBuilder sb = new StringBuilder()
                .append("Content-Length: ")
                .append(lengthOfBodyContent)
                .append(NEW_LINE);
        return sb.toString();
    }

    private static String generateCookie(boolean bool) {
        StringBuilder sb = new StringBuilder()
                .append("Set-Cookie: logined=")
                .append(bool)
                .append("; Path=/")
                .append(NEW_LINE);
        return sb.toString();
    }
}
