package request;

import java.util.HashMap;

import util.SplitUtils;

public class GeneralHeaderValue {
	private HashMap<String, String> generalHeaderValues;

	public GeneralHeaderValue() {
		generalHeaderValues = new HashMap<String, String>();
	}

	public void addGeneralHeaderValue(String line) {
		// 필드값은 대문자로 저장
		if (line.contains(":")) {
			generalHeaderValues.put(SplitUtils.getSplitedValue(line, ": ", 0).toUpperCase(),
					SplitUtils.getSplitedValue(line, ": ", 1));
		}
	}

	public void addGeneralHeaderValue(String field, String text) {
		generalHeaderValues.put(field, text);
	}

	public void addGeneralHeaderValue(GeneralHeaderValue addedValues) {
		generalHeaderValues.putAll(addedValues.getGeneralHeaderValue());
	}

	public HashMap<String, String> getGeneralHeaderValue() {
		return generalHeaderValues;
	}

	public String getTextFindByField(String field) {
		return SplitUtils.valueToStringOrEmpty(generalHeaderValues, field.toUpperCase());
	}

	public String toString(String field) {
		return field + ": " + getTextFindByField(field);
	}
}