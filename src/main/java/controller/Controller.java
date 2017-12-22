package controller;

import request.GeneralHeaderValue;
import request.RequestHeader;

public interface Controller {
	static String HOMEPATH = "/index.html";

	public String run(RequestHeader requestHeader);
	
	public GeneralHeaderValue getResponseHeaderValue();
}
