package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

class Resource {
    private final String uri;

    Resource(String uri) {
        this.uri = uri;
    }

    byte[] getContent() throws IOException {
        return Files.readAllBytes(new File("./webapp" + uri).toPath());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;
        Resource resource = (Resource) o;
        return Objects.equals(uri, resource.uri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uri);
    }

    @Override
    public String toString() {
        return uri;
    }
}
