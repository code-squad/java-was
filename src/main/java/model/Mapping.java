package model;

import java.util.Objects;

public class Mapping {

    public static final String RESOURCE_MARK = ".css";
    public static final String SPLIT_QUESTION = "\\?";
    public static final String QUESTION_MARK = "?";

    private MethodType method;
    private String path;

    public Mapping(String path, MethodType method) {
        this.method = method;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean isResource() {
        return path.contains(RESOURCE_MARK);
    }

    public static Mapping of(String path, String method) {
        /* GET Method Parameter 존재할 경우에만 동작! */
        if(path.contains(QUESTION_MARK)) {
            path = initParams(path);
        }

        return new Mapping(path, MethodType.obtainMethodType(method));
    }

    private static String initParams(String path) {
        return path.split(SPLIT_QUESTION)[0];
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mapping mapping = (Mapping) o;
        return Objects.equals(method, mapping.method) &&
                Objects.equals(path, mapping.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }
}
