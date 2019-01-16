package model;

public enum RequestMethod {

    POST,
    GET,
    PUT,
    DELETE;

    public static boolean isRequestMethod(String method) {
        for (RequestMethod value : RequestMethod.values()) {
            if(String.valueOf(value).equals(method)) return true;
        }
        return false;
    }
}
