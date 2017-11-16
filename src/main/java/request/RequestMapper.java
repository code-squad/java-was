package request;

import java.util.HashMap;
import java.util.Map;

public interface RequestMapper {
	Map<String, PathHandler> pathMethodStrategy = new HashMap<String, PathHandler>();
	void init();
	public PathHandler getReqestMapper(String requestType);
}