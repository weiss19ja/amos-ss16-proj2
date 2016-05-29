package de.developgroup.mrf.server.rpc;

import static org.junit.Assert.assertEquals;

import de.developgroup.mrf.server.rpc.AbstractJsonRpc2;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import de.developgroup.mrf.server.rpc.JsonRpc2ResponseError;
import org.junit.Before;
import org.junit.Test;

public class JsonRpc2ResponseErrorTest {

    private JsonRpc2ResponseError errorRpc;
    private String expectation = "{\"error\":{\"code\":-32603,\"message\":\"internal error occurs\"},\"jsonrpc\":\"2.0\",\"id\":42}";

    @Before
    public void initialize() {

    }

    @Test
    public void testGetterSetter(){
        errorRpc = new JsonRpc2ResponseError(JsonRpc2ResponseError.CODE_INTERNAL_ERROR,"internal error occurs",42);

        assertEquals(42,errorRpc.getId());
        assertEquals(JsonRpc2ResponseError.CODE_INTERNAL_ERROR,errorRpc.getErrorCode());
        assertEquals("internal error occurs",errorRpc.getErrorMessage());
        assertEquals(AbstractJsonRpc2.JSONRPC_VALUE,errorRpc.getJsonrpc());
    }

    @Test
    public void testToJsonString(){
        errorRpc = new JsonRpc2ResponseError(JsonRpc2ResponseError.CODE_INTERNAL_ERROR,"internal error occurs",42);
        assertEquals(expectation,errorRpc.toString());
    }

    @Test
    public void testParseFromString(){
        errorRpc = errorRpc.parse(expectation);

        assertEquals(AbstractJsonRpc2.JSONRPC_VALUE,errorRpc.getJsonrpc());
        assertEquals(42,errorRpc.getId());
        assertEquals(JsonRpc2ResponseError.CODE_INTERNAL_ERROR,errorRpc.getErrorCode());
        assertEquals("internal error occurs",errorRpc.getErrorMessage());
    }

}
