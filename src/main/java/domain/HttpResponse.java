package domain;

public class HttpResponse {
    HttpStatusCode status;

    public HttpResponse(HttpStatusCode status) {
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
