package controller;

public abstract class AbstractController {
    private static final String HTML_EXTENTION = ".html";
    private static final String CSS_DIRECTIVE = ",*/*;q=0.1";
    private static final String HTML_DIRECTIVE = ";charset=utf-8";

    static String makeHtmlUrl(String uri) {
        if (uri.endsWith(HTML_EXTENTION))
            return uri;
        return uri + HTML_EXTENTION;
    }

    static String makeContentType(String acceptType) {
        if (acceptType.endsWith("/css"))
            return acceptType + CSS_DIRECTIVE;
        return acceptType + HTML_DIRECTIVE;
    }
}
