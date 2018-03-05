package webserver;

import java.io.DataOutputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputHandler {
	private static final Logger log = LoggerFactory.getLogger(OutputHandler.class);

	public static DataOutputStream doResponse(OutputStream out) {
		return new DataOutputStream(out);
	}
}
