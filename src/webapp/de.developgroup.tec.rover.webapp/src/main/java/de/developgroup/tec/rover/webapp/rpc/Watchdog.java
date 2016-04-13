package de.developgroup.tec.rover.webapp.rpc;

import java.util.LinkedList;
import java.util.List;

public class Watchdog extends Thread {

	private final long timeoutMS;
	private boolean isReset;
	private boolean shutdown;

	private final List<WatchdogExpirationHandler> callbacks;

	public Watchdog(long timeoutMS) {
		this.timeoutMS = timeoutMS;
		callbacks = new LinkedList<WatchdogExpirationHandler>();
	}

	public synchronized void addHandler(WatchdogExpirationHandler handler) {
		callbacks.add(handler);
	}

	public synchronized void removeHandler(WatchdogExpirationHandler handler) {
		callbacks.remove(handler);
	}

	@Override
	public void run() {
		shutdown = false;

		synchronized (this) {
			while (!shutdown) {
				try {
					isReset = false;
					wait(timeoutMS);
				} catch (InterruptedException e) {
					// Ignore
				}
				if (!isReset && !shutdown) {
					fireWatchdogExpired();
				}
			}
		}
	}

	protected void fireWatchdogExpired() {
		callbacks.parallelStream().forEach(it -> it.handleWatchdogExpired());
	}

	public synchronized void reset() {
		isReset = true;
		notify();
	}

	public synchronized void shutdown() {
		shutdown = true;
		notify();
	}

}
