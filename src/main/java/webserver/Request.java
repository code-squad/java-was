package webserver;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {



    public Request(BufferedReader br) throws IOException {
        String line;
        while(true) {
            line = br.readLine();
            if (line.equals("")) {
                break;
            }
        }

    }
}
