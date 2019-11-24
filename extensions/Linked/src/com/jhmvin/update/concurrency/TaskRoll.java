package com.jhmvin.update.concurrency;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class TaskRoll {
	private final Timer timer;
	private final Handler handler;

	//
	private TimerTask timerTask;
	private IkotIkotTask task;
	private long interval;

	//
	private boolean running;

	public TaskRoll() {
		this.timer = new Timer();
		this.handler = new Handler();
		// default
		this.running = true;
	}

	/**
	 * The task to repeat.
	 * 
	 * @param task
	 */
	public void setTask(IkotIkotTask task) {
		this.task = task;
	}

	/**
	 * Interval on very repeat.
	 * 
	 * @param interval
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * Start the event.
	 */
	public void startTask() {
		this.timerTask = new TimerTask() {
			//
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {
					//
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (isRunning()) {
							task.repeatMe();
						}
					}
				});
			}

		};
		this.timer.scheduleAtFixedRate(this.timerTask, 0, this.interval);
	}

	/**
	 * The number of cancelled task in the queue.
	 * 
	 * @return
	 */
	public int purge() {
		return this.timer.purge();
	}

	/**
	 * Stops all scheduled loop.
	 */
	public void stop() {
		this.timer.cancel();
	}

	// -----------

	/**
	 * Pauses the task, no execution will occur, but still running.
	 */
	public void pause() {
		this.running = false;
	}

	/**
	 * resumes the task if paused.
	 */
	public void resume() {
		this.running = true;
	}

	/**
	 * Tells whether the time is running.
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return this.running;
	}

}
