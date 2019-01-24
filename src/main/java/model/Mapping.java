package model;

import java.util.Objects;

public class Mapping {

    private static final String RESOURCE_MARK = ".css";

    private String method;
    private String path;

    public Mapping(String path, String method) {
        this.method = method;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean isResource() {
        return path.contains(RESOURCE_MARK);
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
