package http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseFactory {
	public static Map<Status, HttpResponse> responses;

	static {
		responses = new HashMap<>();
		responses.put(Status.NUMBER200, Http200Response.create());
		responses.put(Status.NUMBER302, Http302Response.create());
	}

	public static HttpResponse getResponse(Status number) {
		return responses.get(number);
	}
}
