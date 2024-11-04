package com.drozal.dataterminal.util.server;

@FunctionalInterface
public interface ServerStatusListener {
	void onStatusChanged(boolean isConnected);
}
