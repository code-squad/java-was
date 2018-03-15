package webserver;

public enum HttpMethod {
    GET, POST;

    public boolean isGet(){
        return this == HttpMethod.GET;
    }

    public boolean isPost(){
        return this == HttpMethod.POST;
    }
}
