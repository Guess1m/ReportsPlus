package com.Guess.ReportsPlus.util.Misc.Threading;

import com.Guess.ReportsPlus.util.Misc.LogUtils;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;

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
		log("Running Thread: [" + name + "]", LogUtils.Severity.DEBUG);
		runnable.run();
		log("Thread: [" + name + "] Has Finished", LogUtils.Severity.DEBUG);
		ThreadManager.workerThreads.remove(this);
	}
}
