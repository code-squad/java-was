package com.larry.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    public static byte[] resolve(String viewFileName) throws IOException {
        log.info("view file name is {}", viewFileName);
        if (viewFileName.contains("redirect")) {
            viewFileName = viewFileName.replace("redirect:/", "");
        }
        return Files.readAllBytes(new File("webapp/" + viewFileName).toPath());
    }
}
