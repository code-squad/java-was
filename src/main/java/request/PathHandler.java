package request;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestParser;

public interface PathHandler {
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException;
}
