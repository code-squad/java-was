package model;

public enum MethodType {

    GET("GET"), POST("POST"), NOT("NOT");

    private String methodType;

    MethodType(String methodType) {
        this.methodType = methodType;
    }

    public static MethodType obtainMethodType(String methodName) {
        for (MethodType value : values()) {
            if(value.methodType.equals(methodName)) {
                return value;
            }
        }
        return NOT;
    }
}
