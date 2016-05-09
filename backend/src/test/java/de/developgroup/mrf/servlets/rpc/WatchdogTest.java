package de.developgroup.mrf.servlets.rpc;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class WatchdogTest {

	private Watchdog watchdog;
	private WatchdogExpirationHandler handler;

	@Before
	public void setUp() throws Exception {
		watchdog = new Watchdog(500L);
		handler = mock(WatchdogExpirationHandler.class);
		watchdog.addHandler(handler);
	}

	@After
	public void tearDown() throws Exception {
		watchdog.shutdown();
	}

	@Ignore
	@Test
	public void testReset() throws InterruptedException {
		watchdog.start();

		for (int i = 0; i < 20; i++) {
			watchdog.reset();
			Thread.sleep(10L);	// FIXME Test may fail if system is under high load!
		}

		verifyZeroInteractions(handler);
	}

	@Ignore
	@Test
	public void testNoReset() throws InterruptedException {
		watchdog.start();
		// FIXME Test may fail if system is under high load!
		Thread.sleep(600L);
		
		verify(handler).handleWatchdogExpired();
	}

	
}
