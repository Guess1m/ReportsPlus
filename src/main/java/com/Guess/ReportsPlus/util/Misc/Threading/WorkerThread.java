package com.Guess.ReportsPlus.util.Misc.Threading;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;

public class WorkerThread extends Thread {
	String name;
	Runnable runnable;
	
	public WorkerThread(String name, Runnable runnable) {
		this.name = name;
		this.runnable = runnable;
		
		this.setName(name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public void run() {
		ThreadManager.workerThreads.add(this);
		logDebug("Running Thread: [" + name + "]");
		long startTime = System.currentTimeMillis();
		try {
			runnable.run();
			if (Thread.currentThread().isInterrupted()) {
				logWarn("Thread [" + name + "] was interrupted.");
			}
		} catch (Exception e) {
			logError("Thread [" + name + "] encountered an error: " + e.getMessage());
		} finally {
			long endTime = System.currentTimeMillis();
			logDebug("Thread: [" + name + "] Has Finished Runtime: [" + (endTime - startTime) / 1000 + " sec]");
			ThreadManager.workerThreads.remove(this);
		}
	}
}
