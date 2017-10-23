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

public class IndexController extends GetController {
    
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    
    @Override
    public void run(HttpRequest req, HttpResponse res) {
        HttpHeader header;
        log.debug("index controller initiated!");
        try {
        byte[] filebytes = Files.readAllBytes(new File("./webapp/index.html").toPath());
        header = new HttpOkHeader(filebytes.length, false);
        
       
        res.setHeader(header.generateHttpHeaderString());
        res.responseBody(filebytes);
        }
        catch(Exception e) {
            log.error(e.getMessage());
            header = new HttpClientErrorHeader();
            res.setHeader(header.generateHttpHeaderString());
        }
    }
}
