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
		pathStrategy.put("/user/list", new GetUserListPathHandler());
		pathStrategy.put("default", new GetDefaultPathHandler());
	}

	@Override
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		// TODO Auto-generated method stub
		String url = parser.getUrl();
		if(pathStrategy.containsKey(url)) {
			pathStrategy.get(url).run(parser, dos);
		}else {
			pathStrategy.get("default").run(parser, dos);
		}
	}

}
