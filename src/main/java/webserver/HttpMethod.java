package webserver;

public class HttpMethod {

    private String method;

    public HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public boolean isGetMethod(){
        return method.equals("GET");
    }
}
