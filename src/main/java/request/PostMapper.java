package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import post.PostCreateUserPathHandler;
import post.PostLoginPathHandler;

public class PostMapper implements RequestMapper {
	private static final Logger log = LoggerFactory.getLogger(PostMapper.class);
	public PostMapper() {
		init();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		log.debug("POST : help");
		pathMethodStrategy.put("/user/login", new PostLoginPathHandler());
		pathMethodStrategy.put("/user/create", new PostCreateUserPathHandler());
	}

	@Override
	public PathHandler getReqestMapper(String url) {
		return pathMethodStrategy.get(url);
	}

}
