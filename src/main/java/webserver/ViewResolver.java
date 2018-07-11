package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    public byte[] getResource(String path) throws IOException {
        return Files.readAllBytes(new File("./webapp" + path).toPath());
    }
}
