package request;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestParser;

public interface RequestMapper {
	Map<String, RequestMapper> requestMethodStrategy = new HashMap<String, RequestMapper>();
	Map<String, PathHandler> pathStrategy = new HashMap<String, PathHandler>();
	public void init();
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException;
}
