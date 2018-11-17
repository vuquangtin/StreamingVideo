package com.org.videomonitor.ex.main;

import java.util.Calendar;

public class VideoMonitor {

	public static void main(String[] args) {
		VideoMonitor object = new VideoMonitor();
		object.waitMethod();

	}

	private synchronized void waitMethod() {

		while (true) {
			System.out.println("always running program ==> " + Calendar.getInstance().getTime());
			try {
				this.wait(2000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

	}
}
