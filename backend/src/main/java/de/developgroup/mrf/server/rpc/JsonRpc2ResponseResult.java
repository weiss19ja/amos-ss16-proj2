package de.developgroup.mrf.server.rpc;

import com.google.gson.Gson;

/**
 * Represents a JSON-RPC 2.0 response with an result.
 *
 +
 *
 * Sample JSON-RPC 2.0 result response string:
 * <pre>
 * {
 *    "result"  : 42,
 *    "jsonrpc" : "2.0",
 *    "id"      : 12
 * }
 * </pre>
 *
 */
public class JsonRpc2ResponseResult extends AbstractJsonRpc2{

    private Object result;

    public JsonRpc2ResponseResult(final Object result, final long id){
        setResult(result);
        this.id = id;
    }

    public static JsonRpc2ResponseResult parse(String jsonInString) {
        Gson gson = new Gson();
        JsonRpc2ResponseResult jsonObj = gson.fromJson(jsonInString, JsonRpc2ResponseResult.class);
        return jsonObj;
    }

    /**
     * Gets the id of the result response.
     *
     * @return The data of the result response.
     */
    public Object getResult(){
        return result;
    }

    /**
     * Set the id of the result response.
     *
     * @param result The data of the result response.
     */
    public void setResult(final Object result){
        if(!assertNotNull(result)){
            throw new IllegalArgumentException("Result must not be null!");
        }
        this.result = result;
    }

    /**
     * Check if JSON-RPC-Response object ist valid.
     * To be valid JSON-RPC version must be 2.0 and
     * result must not be null.
     *
     * @return True if object is a valid JSON-RPC object.
     */
    @Override
    public boolean isValid() {
        boolean isVersionValid = jsonrpc.equals(AbstractJsonRpc2.JSONRPC_VALUE);
        boolean isResultValid = result != null;
        return isVersionValid && isResultValid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonRpc2ResponseResult)) return false;

        JsonRpc2ResponseResult that = (JsonRpc2ResponseResult) o;

        return result != null ? result.equals(that.result) : that.result == null;

    }

    @Override
    public int hashCode() {
        return result != null ? result.hashCode() : 0;
    }
}
