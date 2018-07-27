package webserver;

import webserver.annotations.LattyFramework;

@LattyFramework
public class WebServer {

    public static void main(String args[]) {
        Latty.run(WebServer.class, args);
    }
}
