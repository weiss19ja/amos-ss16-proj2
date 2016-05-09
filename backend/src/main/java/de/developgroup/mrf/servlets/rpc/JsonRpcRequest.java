package de.developgroup.mrf.servlets.rpc;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.StringUtil;

public class JsonRpcRequest {

	private String jsonrpc;
	private BigInteger id;
	private String method;
	private List<Object> params;

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

	public Map<String, String> validate() {
		Map<String, String> result = new HashMap<String, String>();

		if (!JsonRpcSocket.JSON_RPC_VERSION.equals(jsonrpc)) {
			result.put("jsonrpc", "Invalid version: " + jsonrpc);
		}

		if (StringUtil.isBlank(method)) {
			result.put("method", "No method provided");
		}

		return result;
	}

}
