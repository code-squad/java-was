package webserver;

public enum StatusCode {

    OK(200), FOUND(302);

    private int statusCode;

    private StatusCode(int statusCode){
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return Integer.toString(statusCode) + " " + this.name();
    }
}
