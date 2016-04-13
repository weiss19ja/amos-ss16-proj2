package de.developgroup.tec.rover.webapp.rpc;

import java.math.BigInteger;

public abstract class AbstractJsonRpcReply {

	private final String jsonrpc = JsonRpcSocket.JSON_RPC_VERSION;
	private final BigInteger id;

	public AbstractJsonRpcReply(BigInteger id) {
		this.id = id;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public BigInteger getId() {
		return id;
	}

}
