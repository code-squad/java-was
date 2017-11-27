package http.api.readbodycontroller;

import java.io.IOException;

public interface ReadBodyController {
	public byte[] readBody(String path) throws IOException;
}