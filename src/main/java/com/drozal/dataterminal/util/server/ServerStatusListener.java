package com.drozal.dataterminal.util.server;

/**
 * The interface Server status listener.
 */
@FunctionalInterface
public interface ServerStatusListener {
	/**
	 * On status changed.
	 *
	 * @param isConnected the is connected
	 */
	void onStatusChanged(boolean isConnected);
}
