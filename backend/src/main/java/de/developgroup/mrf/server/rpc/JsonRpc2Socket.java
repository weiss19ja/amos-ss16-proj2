package de.developgroup.mrf.server.rpc;

import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 *
 * This is an implementation of the JSON RPC protocol version 2.0. See
 * http://www.jsonrpc.org/specification for details.
 *
 * This implementation supports only protocol version 2.0, with ids of type
 * long, and parameters as array.
 *
 */
public class JsonRpc2Socket extends WebSocketAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(JsonRpc2Socket.class);

    public JsonRpc2Socket(){

    }


    @Override
    public void onWebSocketConnect(final Session sess) {
        super.onWebSocketConnect(sess);
        LOGGER.debug("Socket connected: {}", sess);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        LOGGER.debug("Socket closed: [{}] {}", statusCode, reason);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        LOGGER.error("Socket error: " + cause.getMessage(), cause);
    }

    @Override
    public void onWebSocketText(String message) {
        LOGGER.debug("Received message: {}", message);

        String responseMsg = processMessage(message);
        if(!responseMsg.isEmpty()) {
            try {
                LOGGER.debug("Sending response: {}", responseMsg);
                getRemote().sendString(responseMsg);
            } catch (IOException e) {
                LOGGER.error("Could not send response: " + e.getMessage(), e);
            }
        }
    }

    protected String processMessage(String message) {
        JsonRpc2Request request;
        String response = "";

        try {
            request = JsonRpc2Request.parse(message);

            if(request.isValid()){
                // handle valid requests
                Object result;

                result = doInvokeMethod(request.getMethod(),request.getParams());

                if (!request.isNotification()){
                    if (result == null){
                        result = "ok";
                    }
                    response = new JsonRpc2ResponseResult(result,request.getId()).toJsonString();
                }
            } else {
                // handle invalid requests
                response = new JsonRpc2ResponseError(JsonRpc2ResponseError.CODE_INVALID_REQUEST,"Invalid request: "+message,request.getId()).toJsonString();
            }

        } catch (JsonSyntaxException e) {
            String errorMessage = "The request could not be parsed: " + e.getMessage();
            response = new JsonRpc2ResponseError(JsonRpc2ResponseError.CODE_PARSE_ERROR,errorMessage).toJsonString();
        } catch (NoSuchMethodException | IllegalAccessException e){
            response = new JsonRpc2ResponseError(JsonRpc2ResponseError.CODE_METHOD_NOT_FOUND,e.getMessage()).toJsonString();
        } catch (InvocationTargetException e ){
            response = new JsonRpc2ResponseError(JsonRpc2ResponseError.CODE_INTERNAL_ERROR,e.getCause().getMessage()).toJsonString();
        }

        return response;
    }

    private Object doInvokeMethod(String method, List<Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object result;
        if(params == null || params.size() == 0){
            result = MethodUtils.invokeMethod(this,method);
        } else {
            result = MethodUtils.invokeMethod(this,method,params.toArray());
        }

        return result;
    }
}
