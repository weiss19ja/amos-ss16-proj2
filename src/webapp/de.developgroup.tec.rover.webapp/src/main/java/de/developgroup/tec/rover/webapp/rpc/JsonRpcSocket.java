package de.developgroup.tec.rover.webapp.rpc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * This is an implementation of the JSON RPC protocol version 2.0. See http://www.jsonrpc.org/specification for details.
 * 
 * This implementation supports only protocol version 2.0, with ids of type integer, and positional parameters.
 *
 */
public abstract class JsonRpcSocket extends WebSocketAdapter {

	static final String JSON_RPC_VERSION = "2.0";

	static final int CODE_PARSE_ERROR = -32700;
	static final int CODE_INVALID_REQUEST = -32600;
	static final int CODE_METHOD_NOT_FOUND = -32601;
	static final int CODE_INVALID_PARAMETERS = -32602;
	static final int CODE_INTERNAL_ERROR = -32603;

	private static final Logger LOG = LoggerFactory.getLogger(JsonRpcSocket.class);

	@Override
	public void onWebSocketConnect(final Session sess) {
		super.onWebSocketConnect(sess);
		LOG.info("Socket connected: {}", sess);
	}

	@Override
	public void onWebSocketText(String message) {
		LOG.trace("Received message: {}", message);

		String replyStr = processMessage(message);

		try {
			// TODO Make async?
			LOG.trace("Sending reply: {}", replyStr);
			getRemote().sendString(replyStr);
		} catch (IOException e) {
			LOG.error("Could not send reply: " + e.getMessage(), e);
		}
	}

	protected String processMessage(String message) {
		JsonRpcRequest request;
		AbstractJsonRpcReply reply;

		try {

			request = new Gson().fromJson(message, JsonRpcRequest.class);

			Map<String, String> validationResult = request.validate();

			if (validationResult.isEmpty()) {

				Object result;

				try {

					Object[] params;

					if (request.getParams() instanceof List) {
						params = ((List<?>) request.getParams()).toArray();
					} else {
						if (request.getParams() != null) {
							params = new Object[] { request.getParams() };
						} else {
							params = new Object[] {};
						}
					}

					result = MethodUtils.invokeMethod(this, request.getMethod(), params);
					reply = new JsonRpcReply(request.getId(), result);

				} catch (NoSuchMethodException | IllegalAccessException e) {
					reply = new JsonRpcErrorReply(request.getId(), CODE_METHOD_NOT_FOUND, e.getMessage(), null);
				} catch (InvocationTargetException e) {
					reply = new JsonRpcErrorReply(request.getId(), CODE_INTERNAL_ERROR, e.getCause().getMessage(),
							null);
				}
			} else {
				reply = new JsonRpcErrorReply(request.getId(), CODE_INVALID_REQUEST, "Invalid request",
						validationResult);
			}

		} catch (JsonSyntaxException e) {
			reply = new JsonRpcErrorReply(null, CODE_PARSE_ERROR, "The request could not be parsed: " + e.getMessage(),
					null);
		}

		String replyStr = new Gson().toJson(reply);
		return replyStr;
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		LOG.info("Socket closed: [{}] {}", statusCode, reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		LOG.error("Socket error: " + cause.getMessage(), cause);
	}

}
