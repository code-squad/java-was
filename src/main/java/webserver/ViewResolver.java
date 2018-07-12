package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    public static byte[] resolve(String viewFileName) throws IOException {
        if (viewFileName.contains("redirect")) {
            viewFileName = viewFileName.replace("redirect:/", "");
        }
        return Files.readAllBytes(new File("webapp/" + viewFileName).toPath());
    }
}
