package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

class Resource {
    private final String path;

    Resource(String uri) {
        this.path = uri;
    }

    byte[] get() throws IOException {
        return Files.readAllBytes(new File("./webapp" + path).toPath());
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
