package de.developgroup.mrf.servlets.rpc;

import java.math.BigInteger;

public class JsonRpcErrorReply extends AbstractJsonRpcReply {

	private final JsonRpcError error;

	public JsonRpcErrorReply(BigInteger id, int code, String message, Object data) {
		super(id);
		error = new JsonRpcError(code, message, data);
	}

	public JsonRpcError getError() {
		return error;
	}

}
