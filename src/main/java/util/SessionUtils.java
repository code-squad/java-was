package util;

import java.util.UUID;

public class SessionUtils {

    public static String createSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
