package de.developgroup.mrf.server.handler;

public interface SingleDriverHandler {

	public void acquireDriver(int clientId);

	public void releaseDriver(int clientId);

	public void verifyDriverAvailability();

}
