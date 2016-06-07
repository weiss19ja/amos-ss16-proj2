package de.developgroup.mrf.server.rpc;


import com.google.gson.Gson;

/**
 * Represents a JSON-RPC 2.0 response with an error.
 *
 +
 *
 * Sample JSON-RPC 2.0 error response string:
 * <pre>
 * {
 *    "error"  : "{"code": -32601, "message": "Method not found"}",
 *    "jsonrpc" : "2.0",
 *    "id"      : 12
 * }
 * </pre>
 *
 */
public class JsonRpc2ResponseError extends AbstractJsonRpc2 {

    public static final int CODE_PARSE_ERROR = -32700;
    public static final int CODE_INVALID_REQUEST = -32600;
    public static final int CODE_METHOD_NOT_FOUND = -32601;
    public static final int CODE_INVALID_PARAMETERS = -32602;
    public static final int CODE_INTERNAL_ERROR = -32603;

    private JsonRpc2ErrorObject error;

    public JsonRpc2ResponseError(final int errorCode, final String errorMessage){
        this.error = new JsonRpc2ErrorObject(errorCode,errorMessage);
    }

    public JsonRpc2ResponseError(final int errorCode, final String errorMessage, final long id){
        this(errorCode,errorMessage);
        this.id = id;
    }

    public static JsonRpc2ResponseError parse(String jsonInString) {
        Gson gson = new Gson();
        JsonRpc2ResponseError jsonObj = gson.fromJson(jsonInString, JsonRpc2ResponseError.class);
        return jsonObj;
    }

    /**
     * Gets the error code.
     *
     * Possible error codes declared as public static finals.
     *
     * @return error code.
     */
    public int getErrorCode(){
        return error.getCode();
    }

    /**
     * Gets the error message.
     *
     * @return error message as text.
     */
    public String getErrorMessage(){
        return error.getMessage();
    }

    /**
     * Check if JSON-RPC-Response object ist valid.
     * To be valid JSON-RPC version must be 2.0 and
     * error must not be null.
     *
     * @return True if object is a valid JSON-RPC object.
     */
    @Override
    public boolean isValid() {
        boolean isVersionValid = jsonrpc.equals(AbstractJsonRpc2.JSONRPC_VALUE);
        boolean isErrorValid = (error != null);
        return isVersionValid && isErrorValid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonRpc2ResponseError)) return false;

        JsonRpc2ResponseError that = (JsonRpc2ResponseError) o;

        return error != null ? error.equals(that.error) : that.error == null;

    }

    @Override
    public int hashCode() {
        return error != null ? error.hashCode() : 0;
    }

    private class JsonRpc2ErrorObject{
        private int code;
        private String message;

        public JsonRpc2ErrorObject(){

        }

        public JsonRpc2ErrorObject(final int errorCode,final String errorMessage){
            this.code = errorCode;
            this.message = errorMessage;
        }

        public int getCode(){
            return code;
        }

        public void setCode(final int errorCode){
            this.code = errorCode;
        }

        public String getMessage(){
            return message;
        }

        public void setMessage(final String errorMessage){
            this.message = errorMessage;
        }

    }
}
