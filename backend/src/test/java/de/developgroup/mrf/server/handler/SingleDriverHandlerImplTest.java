/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
package de.developgroup.mrf.server.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.developgroup.mrf.server.ClientManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.developgroup.mrf.server.ClientManagerImpl;

public class SingleDriverHandlerImplTest {

	SingleDriverHandlerImpl singleDriverHandler;
	ClientManager clientManager;
	RoverHandler roverHandler;

	int clientId = 5432;
	int secondClientId = 5001;

	@Before
	public void setUp() {
		clientManager = mock(ClientManagerImpl.class);
		roverHandler = mock(RoverHandler.class);

		singleDriverHandler = new SingleDriverHandlerImpl(clientManager,
				roverHandler);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testAcquireDriver() {
		singleDriverHandler.acquireDriver(clientId);
		assertEquals(clientId, singleDriverHandler.getCurrentDriverId());

		singleDriverHandler.acquireDriver(secondClientId);
		assertEquals(clientId, singleDriverHandler.getCurrentDriverId());

		singleDriverHandler.acquireDriver(clientId);
		assertEquals(clientId, singleDriverHandler.getCurrentDriverId());
	}

	@Test
	public void testReleaseDriver() {
		singleDriverHandler.acquireDriver(clientId);
		assertEquals(clientId, singleDriverHandler.getCurrentDriverId());
		singleDriverHandler.releaseDriver(clientId);
		assertEquals(-1, singleDriverHandler.getCurrentDriverId());

		// should not release driver mode with another id
		singleDriverHandler.acquireDriver(clientId);
		singleDriverHandler.releaseDriver(secondClientId);
		assertEquals(clientId, singleDriverHandler.getCurrentDriverId());
	}

	@Test
	public void testVerifyDriverAvailability() {
		singleDriverHandler.acquireDriver(clientId);
		// another client is gone
		when(clientManager.isClientConnected(clientId)).thenReturn(true);
		singleDriverHandler.verifyDriverAvailability();
		assertEquals(clientId, singleDriverHandler.getCurrentDriverId());
		// client in driver mode is gone
		when(clientManager.isClientConnected(clientId)).thenReturn(false);
		singleDriverHandler.verifyDriverAvailability();
		assertEquals(-1, singleDriverHandler.getCurrentDriverId());
	}

}
