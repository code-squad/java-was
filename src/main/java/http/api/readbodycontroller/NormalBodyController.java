package http.api.readbodycontroller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NormalBodyController implements ReadBodyController {

	@Override
	public byte[] readBody(String path) throws IOException {
		return Files.readAllBytes(new File("./webapp" + path).toPath());
	}

}
