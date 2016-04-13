package de.developgroup.tec.rover.webapp.rpc;

public class JsonRpcError {

	private final String message;
	private final Object data;
	private final int code;

	public JsonRpcError(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

}
