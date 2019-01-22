package model;

public enum Annotation {

    GET_MAPPING("Getmapping", "GET"),
    PUT_MAPPING("Putmapping", "PUT"),
    POST_MAPPING("PostMapping", "POST"),
    DELETE_MAPPING("DeleteMapping", "DELETE");


    private String name;
    private String method;

    Annotation (String name, String method) {
        this.name = name;
        this.method = method;
    }
}
