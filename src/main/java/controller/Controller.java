package controller;

import request.RequestHeader;
import request.ResponseHeaderValues;

public interface Controller {
	static String HOMEPATH = "/index.html";

	public String run(RequestHeader requestHeader, ResponseHeaderValues responseHeaderValues);
}
