package com.drozal.dataterminal.util.Server;

@FunctionalInterface
public interface ServerStatusListener {
	void onStatusChanged(boolean isConnected);
}
