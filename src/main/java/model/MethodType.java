package model;

import setting.GetMapping;
import setting.PostMapping;

import java.lang.reflect.Method;

public enum MethodType {

    GET("GET", GetMapping.class), POST("POST", PostMapping.class), NOT("NOT", null);

    private String methodType;
    private Class clazz;

    MethodType(String methodType, Class clazz) {
        this.methodType = methodType;
        this.clazz = clazz;
    }

    public static MethodType obtainMethodTypeByName(String methodName) {
        for (MethodType value : values()) {
            if(value.methodType.equals(methodName)) {
                return value;
            }
        }
        return NOT;
    }

    public static MethodType obtainMethodTypeByClass(Class clazz) {
        for (MethodType value : values()) {
            if(value.clazz == clazz) {
                return value;
            }
        }
        return NOT;
    }

    public static Class obtainMethodClass(Method method) {
        for (MethodType value : MethodType.values()) {
            if(method.getDeclaredAnnotation(value.clazz) != null) {
                return value.clazz;
            }
        }
        return NOT.clazz;
    }
}
