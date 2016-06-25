/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.rpc;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * Represents a JSON-RPC 2.0 request or notification.
 *
 * Notification is a request without an id and doesn't expected a response.
 *
 * Sample JSON-RPC 2.0 request string:
 * 
 * <pre>
 * {
 *    "method"  : "ping",
 *    "params"  : [42],
 *    "jsonrpc" : "2.0",
 *    "id"      : 12
 * }
 * </pre>
 *
 *
 * Sample JSON-RPC 2.0 notification string:
 * 
 * <pre>
 * {
 *    "method"  : "heartbeat",
 *    "params"  : [1],
 *    "jsonrpc" : "2.0"
 * }
 * </pre>
 *
 */
public class JsonRpc2Request extends AbstractJsonRpc2 {

	private String method;
	private List<Object> params;

	public JsonRpc2Request(final String method) {
		setMethod(method);
		this.id = null;
	}

	public JsonRpc2Request(final String method, final List<Object> params) {
		setMethod(method);
		this.params = params;
		this.id = null;
	}

	public JsonRpc2Request(final String method, Object param) {
		setMethod(method);

		List<Object> params = new ArrayList<>();
		params.add(param);
		this.params = params;

		this.id = null;
	}

	public JsonRpc2Request(final String method, final long id) {
		setMethod(method);
		this.id = id;
	}

	public JsonRpc2Request(final String method, final List<Object> params,
			final long id) {
		setMethod(method);
		this.params = params;
		this.id = id;
	}

	/**
	 * Check if this JsonRpc2Request object is a JSON-RPC 2.0 Notification
	 *
	 * @return true if request is a notification
	 */
	public boolean isNotification() {
		return (id == null);
	}

	/**
	 * Check if JSON-RPC-Request object ist valid. To be valid JSON-RPC version
	 * must be 2.0 and method must not be null or empty string.
	 *
	 * @return True if object is a valid JSON-RPC object.
	 */
	@Override
	public boolean isValid() {
		boolean isVersionValid = jsonrpc.equals(AbstractJsonRpc2.JSONRPC_VALUE);
		boolean isMethodValid = (method != null) && !method.equals("");
		return isMethodValid && isVersionValid;
	}

	/**
	 * Parse a string representation of a JSON-RPC 2.0 Request into a
	 * JsonRpc2Request object
	 *
	 * @param jsonInString
	 *            String representation of a JSON-RPC 2.0 Request
	 * @return JsonRpc2Request object
	 */
	public static JsonRpc2Request parse(String jsonInString) {
		Gson gson = new Gson();
		JsonRpc2Request jsonObj = gson.fromJson(jsonInString,
				JsonRpc2Request.class);
		return jsonObj;
	}

	/**
	 * Gets the parameters of the request as object list.
	 *
	 * @return parameter as object list.
	 */
	public List<Object> getParams() {
		return params;
	}

	/**
	 * Set parameters of the request.
	 *
	 * @param params
	 *            parameters as object list.
	 */
	public void setParams(List<Object> params) {
		this.params = params;
	}

	/**
	 * Gets the name of the requested method.
	 *
	 * @return name of the method.
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the name of the requested method.
	 *
	 * @param method
	 *            The method name. Must not be {@code null}.
	 */
	public void setMethod(final String method) {
		if (!assertNotNull(method)) {
			throw new IllegalArgumentException("Method name must not be null!");
		}
		this.method = method;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JsonRpc2Request request = (JsonRpc2Request) o;

		if (method != null ? !method.equals(request.method) : request.method != null) return false;
		return params != null ? params.equals(request.params) : request.params == null;

	}

	@Override
	public int hashCode() {
		int result = method != null ? method.hashCode() : 0;
		result = 31 * result + (params != null ? params.hashCode() : 0);
		return result;
	}
}
