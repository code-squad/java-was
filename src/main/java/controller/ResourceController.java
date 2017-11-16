package controller;

import java.io.File;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import header.HttpClientErrorHeader;
import header.HttpHeader;
import header.HttpOkHeader;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class ResourceController extends GetController {

    private static final Logger log = LoggerFactory.getLogger(ResourceController.class);

    @Override
    public void run(HttpRequest req, HttpResponse res) {
        log.debug("hi");
        HttpHeader header;
        try {
            String requestedResource = req.getLine().getPath();
            byte[] filebytes = Files.readAllBytes(new File("./webapp" + requestedResource).toPath());

            if (requestedResource.contains("css")) {
                header = new HttpOkHeader(filebytes.length, true);
            } else {
                header = new HttpOkHeader(filebytes.length, false);
            }

            res.setHeader(header.generateHttpHeaderString());
            res.responseBody(filebytes);
        } catch (Exception e) {
            log.error(e.getMessage());
            header = new HttpClientErrorHeader();
            res.setHeader(header.generateHttpHeaderString());
        }
    }

}
