package request;

import java.io.DataOutputStream;
import java.io.IOException;

import post.PostCreateUserPathHandler;
import post.PostLoginPathHandler;
import util.HttpRequestParser;

public class PostMapper implements RequestMapper {

	public PostMapper() {
		init();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		pathStrategy.put("/user/login", new PostLoginPathHandler());
		pathStrategy.put("/user/create", new PostCreateUserPathHandler());
	}

	@Override
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		// TODO Auto-generated method stub
		String url = parser.getUrl();
		pathStrategy.get(url).run(parser, dos);
	}

}
