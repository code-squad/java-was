package controller;

import request.GeneralHeaderValue;
import request.RequestHeader;

public class HomeController implements Controller {
	private static String HOMEPATH = "/index.html";

	@Override
	public String run(RequestHeader requestHeader) {
		return HOMEPATH;
	}

	@Override
	public GeneralHeaderValue getResponseHeaderValue() {
		return null;
	}
}