package codesquad.model.responses;

public enum ResponseCode {

    OK(200),
    FOUND(302);

    private int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
