package com.drozal.dataterminal.util.Misc;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Prevents the computer from going to sleep by simulating user activity.
 * This class uses the java.awt.Robot class to move the mouse and press a key at regular intervals.
 */
public class sleepUtil {
	
	private Robot robot;
	private boolean running;
	
	/**
	 * Initializes the Robot instance.
	 *
	 * @throws AWTException if the platform configuration does not allow low-level input control
	 */
	public sleepUtil() throws AWTException {
		robot = new Robot();
	}
	
	/**
	 * Starts preventing sleep by simulating mouse movement and key press every 30 seconds.
	 */
	public void startPreventSleep() {
		running = true;
		new Thread(() -> {
			try {
				while (running) {
					Point mousePosition = MouseInfo.getPointerInfo()
					                               .getLocation();
					int x = mousePosition.x;
					int y = mousePosition.y;
					robot.mouseMove(x, y + 1);
					robot.mouseMove(x, y);
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyRelease(KeyEvent.VK_SHIFT);
					System.out.println("ran");
					Thread.sleep(30000);
				}
			} catch (InterruptedException e) {
				Thread.currentThread()
				      .interrupt();
			}
		}).start();
	}
}