package de.developgroup.tec.rover.webapp.rpc;

import java.math.BigInteger;

public class JsonRpcReply extends AbstractJsonRpcReply {

	private final Object result;

	public JsonRpcReply(BigInteger id, Object result) {
		super(id);
		this.result = result;
	}

	public Object getResult() {
		return result;
	}

}
