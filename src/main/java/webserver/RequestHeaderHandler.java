package webserver;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import exception.InvalidMethodTypeException;
import model.User;
import request.RequestHeader;
import request.RequestLine;
import request.ResponseHeader;
import util.HttpRequestUtils;
import util.HttpRequestUtils.RequestMethodType;
import util.SplitUtils;

public class RequestHeaderHandler {

	private static final Logger log = LoggerFactory.getLogger(RequestHeaderHandler.class);

	private String homePath = "/index.html";
	private ArrayList<ResponseHeader> responseHeaders = new ArrayList<ResponseHeader>();

	public String getResponseValue(RequestHeader request) {
		return methodTurningPoint(request);
	}

	public ArrayList<ResponseHeader> getResponseHeaderList() {
		return responseHeaders;
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
			if ("login".equals(splitedPath[2])) {
				return userLogin(requestHeader.getRequestBody());
			}
		}
		return "";
	}

	private void userSignUp(String requestBody) {
		DataBase.addUser(getUser(HttpRequestUtils.parseQueryString(requestBody)));
	}

	private String userLogin(String requestBody) {
		Map<String, String> loginValue = HttpRequestUtils.parseQueryString(requestBody);
		// userId password
		String userId = loginValue.get("userId");
		String password = loginValue.get("password");
		User user = DataBase.findUserById(userId);
		// 로그인 성공
		if (password.equals(user.getPassword())) {
			log.debug(user.getName() + " 로그인 성공");
			responseHeaders.add(new ResponseHeader("Cookie", "logined=true; Path=/"));
			return "redirect: " + homePath;
		}
		log.debug("로그인 실패");
		return "redirect: /user/login_failed.html";
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