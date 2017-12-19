package request;

import java.util.HashMap;

import util.SplitUtils;

public class RequestHeaderValue {
	private HashMap<String, String> requestHeaderValues;

	public RequestHeaderValue() {
		requestHeaderValues = new HashMap<String, String>();
	}

	public void addRequestHeaderValue(String line) {
		// 필드값은 대문자로 저장
		if (line.contains(":")) {
			requestHeaderValues.put(SplitUtils.getSplitedValue(line, ": ", 0).toUpperCase(),
					SplitUtils.getSplitedValue(line, ": ", 1));
		}
	}

	public String getTextFindByField(String field) {
		return SplitUtils.valueToStringOrEmpty(requestHeaderValues, field.toUpperCase());
	}

	public String toString(String field) {
		return field + ": " + getTextFindByField(field);
	}
}