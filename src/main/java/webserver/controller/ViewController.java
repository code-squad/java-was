package webserver.controller;

import webserver.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.response.ResponseHeaderAttribute.CONTENT_LENGTH;
import static webserver.response.ResponseHeaderAttribute.CONTENT_TYPE;

public class ViewController implements Controller {
    private static final String STATIC_FILE_DIR = "./webapp";

    @Override
    public Response process(Request request, Response response) {
        try {
            byte[] body = Files.readAllBytes(new File(STATIC_FILE_DIR + request.getPath()).toPath());
            return response.setStatus(HttpStatus.OK)
                    .setHeader(CONTENT_TYPE, request.getHeader("Accept"))
                    .setHeader(CONTENT_LENGTH, String.valueOf(body.length))
                    .setBody(new String(body));
        } catch (IOException e) {
            return response.setStatus(HttpStatus.NOT_FOUND).setHeader(CONTENT_LENGTH, String.valueOf(0)).setBody(null);
        }
    }
}
