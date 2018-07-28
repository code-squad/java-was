package com.larry;

import com.larry.webserver.Latty;
import com.larry.webserver.annotations.LattyFramework;

@LattyFramework
public class WebServer {

    public static void main(String args[]) {
        Latty.run(WebServer.class, args);
    }
}
