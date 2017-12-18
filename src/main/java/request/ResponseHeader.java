package request;

public class ResponseHeader {
	private String field;
	private String text;

	public ResponseHeader(String field, String text) {
		this.field = field;
		this.text = text;
	}

	@Override
	public String toString() {
		return field + ": " + text;
	}
}
