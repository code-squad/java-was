package webserver;

public enum HttpMethod {
    GET, POST, PUT, DELETE;

    public static HttpMethod ofValue(String method) {
        switch (method) {
            case "GET":
                return GET;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "DELETE":
                return DELETE;
        }
        return null;
    }
}
