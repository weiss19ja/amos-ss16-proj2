package de.developgroup.mrf.servlets.rpc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class JsonRpcSocketTest {

	class CallMock {
		public void method0Throw() {
		}

		public void method0() {
		}

		public void method1(String str) {
		}

		public String method2(float f1, float f2) {
			return null;
		}
	}

	class TestJsonRpcSocket extends JsonRpcSocket {

		final CallMock mock = Mockito.mock(CallMock.class);

		public String invokeProcessMessage(String message) {
			return processMessage(message);
		}

		public void method0Throw() {
			mock.method0Throw();
		}

		public void method0() {
			mock.method0();
		}

		public void method1(String str) {
			mock.method1(str);
		}

		public String method2(Double f1, Double f2) {
			return mock.method2(f1.floatValue(), f2.floatValue());
		}
	}

	TestJsonRpcSocket socket;

	@Before
	public void setUp() throws Exception {
		socket = new TestJsonRpcSocket();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseError() {
		String msg = "Something not JSON";
		String result = socket.invokeProcessMessage(msg);

		assertThat(result, containsString("\"error\":"));
		assertThat(result, containsString("\"code\":-32700"));
		assertThat(result, containsString("\"jsonrpc\":\"2.0\""));
	}

	@Test
	public void testInvalidRequest() {
		String msg = "{\"jsonrpc\":\"1.0\"}";

		String result = socket.invokeProcessMessage(msg);

		assertThat(result, containsString("\"error\":"));
		assertThat(result, containsString("\"code\":-32600"));
		assertThat(result, containsString("\"jsonrpc\":\"2.0\""));
	}

	@Test
	public void testNoSuchMethod() {
		String msg = "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42, 23], \"id\": 1}";

		String result = socket.invokeProcessMessage(msg);

		assertThat(result, containsString("\"error\":"));
		assertThat(result, containsString("\"code\":-32601"));
		assertThat(result, containsString("\"jsonrpc\":\"2.0\""));
	}

	@Test
	public void testException() {
		String msg = "{\"jsonrpc\": \"2.0\", \"method\": \"method0Throw\", \"id\": 1}";

		Mockito.doThrow(IllegalStateException.class).when(socket.mock).method0Throw();
		String result = socket.invokeProcessMessage(msg);
		Mockito.verify(socket.mock).method0Throw();
		

		assertThat(result, containsString("\"error\":"));
		assertThat(result, containsString("\"code\":-32603"));
		assertThat(result, containsString("\"jsonrpc\":\"2.0\""));
	}

	@Test
	public void testMethod0() {
		String msg = "{\"jsonrpc\": \"2.0\", \"method\": \"method0\", \"id\": 1}";
		String result = socket.invokeProcessMessage(msg);
		Mockito.verify(socket.mock).method0();
		assertThat(result, containsString("\"jsonrpc\":\"2.0\""));
	}
	
	@Test
	public void testMethod1() {
		String msg = "{\"jsonrpc\": \"2.0\", \"method\": \"method1\", \"params\": [\"Some String\"], \"id\": 1}";
		String result = socket.invokeProcessMessage(msg);
		Mockito.verify(socket.mock).method1("Some String");
		assertThat(result, containsString("\"jsonrpc\":\"2.0\""));
	}

	
	@Test
	public void testMethod2() {
		String msg = "{\"jsonrpc\": \"2.0\", \"method\": \"method2\", \"params\": [2.0, 34.0], \"id\": 20}";
		Mockito.doReturn("Hello World").when(socket.mock).method2(2.0f, 34.0f);
		String result = socket.invokeProcessMessage(msg);
		
		Mockito.verify(socket.mock).method2(2.0f, 34.0f);
		assertThat(result, containsString("\"jsonrpc\":\"2.0\""));
		assertThat(result, containsString("\"result\":\"Hello World\""));
		
		
	}
}
