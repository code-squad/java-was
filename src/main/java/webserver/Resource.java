package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class Resource {
    private static final Resource EMPTY_RESOURCE = new Resource("");
    private final String path;

    private Resource(String uri) {
        this.path = uri;
    }

    public static Resource of(String uri) {
        return new Resource(uri);
    }

    public static Resource ofEmpty() {
        return EMPTY_RESOURCE;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY_RESOURCE);
    }

    byte[] getBytes() throws IOException {
        if (this.equals(EMPTY_RESOURCE)) {
            return "".getBytes();
        }
        return Files.readAllBytes(new File("./webapp" + path).toPath());
    }

    public int getLength() {
        return path.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;
        Resource resource = (Resource) o;
        return Objects.equals(path, resource.path);
    }

    @Override
    public int hashCode() {

        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return path;
    }
}
