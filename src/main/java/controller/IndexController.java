package controller;

import java.io.File;
import java.io.IOException;
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
    private HttpHeader header;
    
    @Override
    public void run(HttpRequest req, HttpResponse res) {
        try {
        byte[] filebytes = Files.readAllBytes(new File("./webapp/index.html").toPath());
        this.header = new HttpOkHeader();
        
        res.setHeader(this.header.generateHttpHeaderString());
        res.responseBody(filebytes);
        
        }
        catch(Exception e) {
            log.error(e.getMessage());
            this.header = new HttpClientErrorHeader();
            res.setHeader(this.header.generateHttpHeaderString());
        }
    }
}
