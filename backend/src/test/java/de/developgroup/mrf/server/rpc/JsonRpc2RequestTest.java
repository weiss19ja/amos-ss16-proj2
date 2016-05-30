package de.developgroup.mrf.server.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import de.developgroup.mrf.server.rpc.AbstractJsonRpc2;
import de.developgroup.mrf.server.rpc.JsonRpc2Request;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonRpc2RequestTest {

    private JsonRpc2Request request;
    private final String jsonAsString = "{\"method\":\"testMethod\",\"params\":[42,12,33,\"testStr\"],\"jsonrpc\":\"2.0\",\"id\":42}";
    private final String notificationAsString = "{\"method\":\"testMethod\",\"params\":[42,12,33,\"testStr\"],\"jsonrpc\":\"2.0\"}";


    @Before
    public void initialize() {
        request = new JsonRpc2Request("testMethod",42);
    }

    @Test
    public void testGetterSetter(){
        request.setId(1234);
        assertEquals(1234,request.getId());

        request.setMethod("myMethod");
        assertEquals("myMethod",request.getMethod());

        request.setParams(generateParams());
        assertEquals(4,request.getParams().size());
    }

    @Test
    public void testToJsonString(){

        request = new JsonRpc2Request("testMethod",generateParams(),42);

        assertFalse(request.isNotification());
        assertEquals(jsonAsString,request.toJsonString());
    }

    @Test
    public void testNotificationToJsonString(){

        request = new JsonRpc2Request("testMethod",generateParams());

        assertTrue(request.isNotification());
        assertEquals(notificationAsString,request.toJsonString());
    }

    @Test
    public void testParseFromString(){
        request = JsonRpc2Request.parse(jsonAsString);

        assertTrue(request.isValid());
        assertEquals(42,request.getId());
        assertEquals("testMethod",request.getMethod());
        assertEquals(4,request.getParams().size());
    }

    @Test
    public void testGetJsonrpcVersion(){
        assertEquals(request.getJsonrpc(), AbstractJsonRpc2.JSONRPC_VALUE);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullMethod(){
        request = new JsonRpc2Request(null,generateParams(),42);
    }

    private List<Object> generateParams(){
        ArrayList<Object> params = new ArrayList<>();
        params.add(42);
        params.add(12);
        params.add(33);
        params.add("testStr");
        return params;
    }
}
