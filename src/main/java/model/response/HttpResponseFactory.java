package model.response;

public class HttpResponseFactory {

	public static HttpResponse createHttpResponse(int statusNumber) {
		if(statusNumber == 200) {
			return new Http200Response();
		}
		if(statusNumber == 302) {
			return new Http302Response();
		}
		throw new HttpResponseException("해당하는 Response가 없습니다.");
	}
	
}
