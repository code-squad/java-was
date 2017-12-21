package webserver;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.UrlController;
import db.DataBase;
import exception.InvalidMethodTypeException;
import model.User;
import request.RequestHeader;
import request.RequestHeaderValue;
import request.RequestLine;
import request.ResponseHeaderValues;
import util.HttpRequestUtils;
import util.HttpRequestUtils.RequestMethodType;
import util.SplitUtils;

public class RequestHeaderHandler {

	private static final Logger log = LoggerFactory.getLogger(RequestHeaderHandler.class);

	private static String HOMEPATH = "/index.html";
	private ResponseHeaderValues responseHeaders = new ResponseHeaderValues();
	private RequestHeaderValue requestHeaderValue;
	private UrlController urlController = new UrlController();

	public String getResponseValue(RequestHeader request) {
		requestHeaderValue = request.getRequestHeaderValues();
		return methodTurningPoint(request);
	}

	public ResponseHeaderValues getResponseHeaderList() {
		return responseHeaders;
	}

	private boolean isLogin() {
		String cookiesValue = requestHeaderValue.getTextFindByField("Cookie");
		Map<String, String> cookies = HttpRequestUtils.parseCookies(cookiesValue);
		return Boolean.valueOf(SplitUtils.valueToStringOrEmpty(cookies, "logined"));

	}

	private String methodTurningPoint(RequestHeader request) {
		RequestLine requestLine = request.getRequestLine();
		RequestMethodType requestMethodType = requestLine.getMethodType();
		if (RequestMethodType.GET == requestMethodType) {
			return whenGet(requestLine);
		}
		if (RequestMethodType.POST == requestMethodType) {
			return whenPost(request, responseHeaders);
		}
		throw new InvalidMethodTypeException("지원하지 않는 메서드 요청입니다.");
	}

	private String whenPost(RequestHeader requestHeader, ResponseHeaderValues responseHeaderValues) {
		String url = requestHeader.getPathValue();
		log.debug("resolveControllerUrl: " + url);
		Controller resolveController = urlController.resolveController(url);
		return resolveController.run(requestHeader, responseHeaderValues);
	}

	private String userList() {
		StringBuilder stringBuilder = new StringBuilder();
		ArrayList<User> users = new ArrayList<User>();
		users.addAll(DataBase.findAll());

		stringBuilder.append("<!doctype html>");
		stringBuilder.append("<html>");
		stringBuilder.append("<body>");
		for (User user : users) {
			stringBuilder.append(user.toString() + "<br>");
		}
		stringBuilder.append("</body>");
		stringBuilder.append("</html>");
		return stringBuilder.toString();
	}

	private String whenGet(RequestLine requestLine) {
		String requestPath = requestLine.getRequestPath().toString();
		String[] splitedPath = requestPath.split("/");
		if ("/".equals(requestPath))
			return HOMEPATH;
		if (isWebFile(requestPath)) {
			return requestPath;
		}
		if ("user".equals(splitedPath[1]) && "list".equals(splitedPath[2])) {
			log.debug("list 페이지");
			if (isLogin()) {
				return "dataValue: " + userList();
			}
			log.debug("로그인이 되어 있지 않습니다.");
			return "redirect: /user/login.html";
		}
		return "";
	}

	private boolean isWebFile(String url) {
		String extension = SplitUtils.getSplitedExtension(url).toUpperCase();
		return "HTML".equals(extension) || "JS".equals(extension) || "CSS".equals(extension);
	}
}