package controller;

import request.GeneralHeaderValue;
import request.RequestHeader;

public class WebFileController implements Controller {

	@Override
	public String run(RequestHeader requestHeader) {
		return requestHeader.getPathValue();
	}

	@Override
	public GeneralHeaderValue getResponseHeaderValue() {
		return null;
	}

}
