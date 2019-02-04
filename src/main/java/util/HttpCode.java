package util;

public enum HttpCode {
    OK(200,"OK"),
    FOUND(302, "FOUND");

    HttpCode(int code, String explain) {
        this.code = code;
        this.explain = explain;
    }

    private int code;
    private String explain;


    public String getStartLine() {
        return code + " " + explain;
    }

}
