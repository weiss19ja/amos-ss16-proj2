/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.rpc;

import de.developgroup.mrf.server.rpc.JsonRpc2ResponseResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonRpc2ResponseResultTest {

    private JsonRpc2ResponseResult result;
    private String expectation = "{\"result\":54321,\"jsonrpc\":\"2.0\",\"id\":12}";

    @Before
    public void initialize() {

    }

    @Test
    public void testGetterSetter(){
        result = new JsonRpc2ResponseResult(54321,42);

        assertEquals(54321,result.getResult());
        assertEquals(42,result.getId());

        result.setResult(4444);

        result.setId(12);
        assertEquals(4444,result.getResult());
        assertEquals(12,result.getId());
    }

    @Test
    public void testToJsonString(){
        result = new JsonRpc2ResponseResult(54321,12);
        assertEquals(expectation,result.toString());
    }

    @Test
    public void testParseFromString(){
        result = JsonRpc2ResponseResult.parse(expectation);

    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullResult(){
        result = new JsonRpc2ResponseResult(null,42);
    }

}
