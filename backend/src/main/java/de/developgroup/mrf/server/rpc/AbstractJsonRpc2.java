package de.developgroup.mrf.server.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Abstract JSON-RPC class to provide common methods and fields.
 */
public abstract class AbstractJsonRpc2 {

    public static final String JSONRPC_TEXT = "jsonrpc";
    public static final String JSONRPC_VALUE = "2.0";
    public static final String METHOD = "method";
    public static final String ID = "id";
    public static final String PARAMS = "params";
    public static final String ERROR = "error";
    public static final String RESULT = "result";

    protected final String jsonrpc = JSONRPC_VALUE;

    protected Long id;

    protected transient boolean noId = false;

    public abstract boolean isValid();

    /**
     * Get JSON-RPC version
     *
     * @return JSON-RPC version
     */
    public String getJsonrpc(){
        return jsonrpc;
    }

    /**
     * Returns a JSON string representation of this JSON-RPC 2.0 message.
     *
     * @return The JSON-RPC object as string.
     */
    public String toJsonString(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this).toString();
    }

    /**
     * Returns a JSON string representation of this JSON-RPC 2.0 message.
     *
     * @see #toJsonString()
     *
     * @return The JSON-RPC object as string.
     */
    @Override
    public String toString(){
        return toJsonString();
    }

    /**
     * Gets the id of the JSON-RPC message.
     *
     * @return id of the JSON-RPC message.
     */
    public long getId(){
        return id;
    }

    /**
     * Set the id of the JSON-RPC message.
     *
     * @param id The id of the JSON-RPC message.
     */
    public void setId(final long id){
        this.id = id;
    }

    /**
     * Assert method to check if objects are not null.
     *
     * @param obj object to check for null
     * @return true if object is not null
     */
    protected boolean assertNotNull(Object obj){
        return (obj != null);
    }

}
