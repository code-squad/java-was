package webserver;

public enum ContentType {
    HTML("text/html"), CSS("text/css");

    private String contentType;

    private ContentType(String contentType){
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return contentType;
    }
}
