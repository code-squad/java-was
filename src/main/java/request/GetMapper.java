package request;

import java.io.DataOutputStream;
import java.io.IOException;

import get.GetDefaultPathHandler;
import get.GetUserListPathHandler;
import util.HttpRequestParser;

public class GetMapper implements RequestMapper{

	public GetMapper() {
		init();
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		pathMethodStrategy.put("/user/list", new GetUserListPathHandler());
		pathMethodStrategy.put("default", new GetDefaultPathHandler());
	}

	@Override
	public PathHandler getReqestMapper(String requestType) {
		// TODO Auto-generated method stub
		if(pathMethodStrategy.containsKey(requestType)) {
			return pathMethodStrategy.get(requestType);
		}
		
		return pathMethodStrategy.get("default");
	}

}
