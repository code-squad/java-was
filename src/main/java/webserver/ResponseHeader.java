package webserver;

import java.io.DataOutputStream;

import org.slf4j.Logger;

public class ResponseHeader {
    
    public enum ResponseCodes{
        NOT_FOUND_400(400), UNAUTHORIZED_401(401), REDIRECT_302(302), REDIRECT_301(301), OK_200(200);
        
        private int code;
        private ResponseCodes(int code) {
            this.code = code;
        }
        public int getCode() {
            return this.code;
        }
    }
}
