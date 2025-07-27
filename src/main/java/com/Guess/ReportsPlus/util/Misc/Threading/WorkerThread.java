package com.Guess.ReportsPlus.util.Misc.Threading;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;

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
		logDebug("Running Thread: [" + name + "], Active: [" + ThreadManager.workerThreads.size() + "]");
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
			ThreadManager.workerThreads.remove(this);
			logDebug("Thread: [" + name + "] Has Finished Runtime: [" + (endTime - startTime) / 1000
					+ " sec], Active: [" + ThreadManager.workerThreads.size() + "]");
		}
	}

	public void stopThread() {
		interrupt();
	}
}
