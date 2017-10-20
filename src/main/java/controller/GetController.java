package controller;

import header.HttpHeader;
import util.HttpRequestUtils.RequestTypes;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class GetController implements Controller {
    
    private HttpHeader header;
    private RequestTypes type;

    @Override
    public void run(HttpRequest req, HttpResponse res) {
        
    }

    @Override
    public RequestTypes getType() {
        return this.type;
    }
    
   
    

}
