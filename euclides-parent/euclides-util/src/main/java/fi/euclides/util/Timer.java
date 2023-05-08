package fi.euclides.util;

import java.util.TimerTask;

/**
 * GWT compatible Timer class
 * @see java.util.TimerTask
 * @author wim
 *
 */
public abstract class Timer extends TimerTask
{

	private static final java.util.Timer TIMER = new java.util.Timer();
	
	public void scheduleRepeating(int ms) {
		try {
			TIMER.schedule(this, 0, ms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
