package controller;

public abstract class AbstractController {
    private static final String HTML_EXTENTION = ".html";

    static String makeHtmlUrl(String uri) {
        if (uri.endsWith(HTML_EXTENTION))
            return uri;
        return uri + HTML_EXTENTION;
    }
}
