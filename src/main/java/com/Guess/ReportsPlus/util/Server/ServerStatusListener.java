package com.Guess.ReportsPlus.util.Server;

@FunctionalInterface
public interface ServerStatusListener {
	void onStatusChanged(boolean isConnected);
}
