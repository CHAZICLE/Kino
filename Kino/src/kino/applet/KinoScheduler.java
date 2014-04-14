package kino.applet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;

import kino.cache.CacheManager;

public class KinoScheduler extends Thread {
	static KinoScheduler scedge = new KinoScheduler();
	private static LinkedList<KinoTask> renderTickTasks = new LinkedList<>();
	private static LinkedList<KinoTask> controlTickTasks = new LinkedList<>();
	private static LinkedList<KinoTask> asyncTasks = new LinkedList<>();
	private static Logger LOG = Logger.getLogger(KinoScheduler.class.getSimpleName());
	static abstract class KinoTask {
		/**
		 * Called when the task is first scheduled
		 */
		public void onCreate() {
		};

		/**
		 * Called when the task is started to be handled by a worker
		 */
		public void onStart() {
		};

		/**
		 * Called once to perform the schedules tasks work
		 * 
		 * @return true if the work is complete
		 */
		public boolean run() {
			return false;
		}

		/**
		 * Called when the task has completed it work, use this to free up/close
		 * any resources
		 */
		public void onStop() {
		};
	}

	@Override
	public synchronized void start() {
		setName("Kino Schedular dev testing thread");
		super.start();
	}

	@Override
	public void run() {
		LOG.info("Thread "+getName()+" started at "+System.currentTimeMillis());
		while (!Thread.interrupted()) {
			synchronized (asyncTasks) {
				Iterator<KinoTask> it = asyncTasks.iterator();
				for (KinoTask task = it.next(); it.hasNext(); task = it.next()) {
					if (!task.run()) {
						task.onStop();
						it.remove();
					}
				}
			}
		}
		LOG.info("Thread "+getName()+" closed at "+System.currentTimeMillis());
	}

	public static void scheduleRenderTask(KinoTask newTask) {
		synchronized (renderTickTasks) {
			renderTickTasks.add(newTask);
		}
	}

	public static void scheduleControlTask(KinoTask newTask) {
		synchronized (controlTickTasks) {
			controlTickTasks.add(newTask);
		}
	}

	public static void scheduleAsyncTask(KinoTask newTask) {
		synchronized (asyncTasks) {
			asyncTasks.add(newTask);
		}
	}

	public static void scheduleCacheTask(CacheManager cache, KinoTask newTask) {

	}
}
