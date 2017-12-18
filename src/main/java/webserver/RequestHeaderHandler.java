package webserver;

import java.util.Map;

import db.DataBase;
import exception.InvalidMethodTypeException;
import model.User;
import request.RequestHeader;
import request.RequestLine;
import util.HttpRequestUtils;
import util.HttpRequestUtils.RequestMethodType;
import util.SplitUtils;

public class RequestHeaderHandler {
	String homePath = "/index.html";

	public String getResponseValue(RequestHeader request) {
		return methodTurningPoint(request);
	}

	private String methodTurningPoint(RequestHeader request) {
		RequestLine requestLine = request.getRequestLine();
		RequestMethodType requestMethodType = requestLine.getMethodType();
		if (RequestMethodType.GET.equals(requestMethodType)) {
			return whenGet(requestLine);
		}
		if (RequestMethodType.POST.equals(requestMethodType)) {
			return whenPost(request);
		}
		throw new InvalidMethodTypeException("지원하지 않는 메서드 요청입니다.");
	}

	private String whenPost(RequestHeader requestHeader) {
		String[] splitedPath = requestHeader.getPathValue().split("/");
		if ("user".equals(splitedPath[1])) {
			if ("create".equals(splitedPath[2])) {
				userSignUp(requestHeader.getRequestBody());
				return "redirect: " + homePath;
			}
			if("login".equals(splitedPath[2])) {
				
			}
		}
		return "";
	}

	private void userSignUp(String requestBody) {
		DataBase.addUser(getUser(HttpRequestUtils.parseQueryString(requestBody)));
	}

	private User getUser(Map<String, String> inputValue) {
		User user = new User(inputValue.get("userId"), inputValue.get("password"), inputValue.get("name"),
				inputValue.get("email"));
		return user;
	}

	private String whenGet(RequestLine requestLine) {
		String requestPath = requestLine.getRequestPath().toString();
		if ("/".equals(requestPath))
			return homePath;
		if (isWebFile(requestPath)) {
			return requestPath;
		}
		return "";
	}

	private boolean isWebFile(String url) {
		String extension = SplitUtils.getSplitedExtension(url).toUpperCase();
		return "HTML".equals(extension) || "JS".equals(extension) || "CSS".equals(extension);

	}
}