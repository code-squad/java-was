package com.larry.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class ViewResolver {

    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    public static byte[] resolve(String viewFileName, Map<String, Object> model) throws IOException {
        log.info("view file name is {}", viewFileName);
        log.info("model : {}", model);
        return Files.readAllBytes(new File("webapp/" + viewFileName).toPath());
    }
}
