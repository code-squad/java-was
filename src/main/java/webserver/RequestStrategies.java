package webserver;

import java.util.HashMap;
import java.util.Map;

import request.GetMapper;
import request.PathHandler;
import request.PostMapper;
import request.RequestMapper;
import util.HttpRequestParser;

public class RequestStrategies {
	private Map<String, RequestMapper> requsetStrategy;
	public RequestStrategies(){
		requsetStrategy = new HashMap<String, RequestMapper>();
		requsetStrategy.put("GET", new GetMapper());
		requsetStrategy.put("POST", new PostMapper());
	}
	public PathHandler getPathHandler(String requestType, String url) {
		return getRequestMapper(requestType).getReqestMapper(url);
	}
	
	public RequestMapper getRequestMapper(String requestType) {
		return this.requsetStrategy.get(requestType);
	}
}
