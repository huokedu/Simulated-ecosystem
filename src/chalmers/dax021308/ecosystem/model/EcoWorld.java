package chalmers.dax021308.ecosystem.model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ecosystem main class. Extends {@link Observable}
 * 
 * @author Erik
 *
 */
public class EcoWorld extends Observable {
	private AtomicBoolean environmentFinished = new AtomicBoolean(false);
	private AtomicBoolean timerFinished       = new AtomicBoolean(false);
	private AtomicBoolean shouldRun       	  = new AtomicBoolean(false);
	private boolean runWithoutTimer;
	private int numIterations;
	private TimerHandler timer;
	private Environment env;
	private int tickTime;
	/**
	 * Simple object, used for synchronizing the {@link TimerHandler} and the Enviroment {@link OnFinishListener}
	 */
	private Object syncObject = new Object();
	private static final int NUM_THREAD = 1;
	private int numUpdates = 0;
    private ExecutorService executor = Executors.newFixedThreadPool(NUM_THREAD);
	

	private OnFinishListener mOnFinishListener = new OnFinishListener() {
		@Override
		public void onFinish(List<IPopulation> popList, List<Obstacle> obsList) {
			if(runWithoutTimer) {
				scheduleEnvironmentUpdate();				
			} else {
				synchronized (syncObject) {
					Log.v("Environment: Finished.");
					if(timerFinished.get()) {
						Log.v("Environment: Timer is finished, doing Environment update");
						environmentFinished.set(false);
						timerFinished.set(false);
						scheduleEnvironmentUpdate();
					} else {
						Log.v("Environment: Timer NOT finished, waiting...");
						environmentFinished.set(true);
					}
				}
			}
		}
	};
	
	private OnTickUpdate onTickListener = new OnTickUpdate() {
		@Override
		//När timer är klar.
		public void onTick() {
			synchronized (syncObject) {
				Log.v("Timer: Finished.");
				if(environmentFinished.get()) {
					Log.v("Timer: Environment is finished, doing Environment update");
					timerFinished.set(false);
					environmentFinished.set(false);
					scheduleEnvironmentUpdate();
				} else {
					Log.v("Timer: Environment NOT finished, waiting...");
					timerFinished.set(true);
				}
			}
		}
	};
	
	public EcoWorld(int tickTime, int numIterations) {
		this.tickTime = tickTime;
		timer = new TimerHandler();
		env = new Environment(mOnFinishListener);
		runWithoutTimer = false;
		this.numIterations = numIterations; 
	}
	
	public EcoWorld(int numIterations) {
		this(0, numIterations);
		runWithoutTimer = true;
	}
	
	public EcoWorld() {
		this(Integer.MAX_VALUE);
	}
	
	public void start() {
		shouldRun.set(true);
		scheduleEnvironmentUpdate();
		Log.i("EcoWorld started.");
	}
	
	/**
	 * Stops the scheduling algorithms. 
	 * 
	 */
	public void stop() {
		shouldRun.set(false);
		executor.shutdown();
		timer.stop();
		numUpdates = 0;
		Log.i("EcoWorld stopped.");
	}
	
	/**
	 * Starts the {@link TimerHandler} and executes one Environment iteration.
	 */
	private void scheduleEnvironmentUpdate() {
		if(numIterations-- >= 0) {
			if(!runWithoutTimer) {
				timer.start(tickTime, onTickListener);
			}
			Log.v("---- sheduleEnvironmentUpdate() ---- Number of updates:" + ++numUpdates);
			executor.execute(env);				
		} else {
			stop();
		}
	}
	
	/**
	 * Adjust the tick rate of the next iteration.
	 * The currently executing iteration will not be affected.
	 * @param newTickRate
	 */
	public void adjustTickRate(int newTickRate) {
		this.tickTime = newTickRate;
	}
	
	/**
	 * Tick listener for the TimerHandler. Called when timer has expired.
	 * 
	 * @author Erik
	 *
	 */
	public interface OnTickUpdate {
		public void onTick();
	}
	
	public void setRunWithoutTimer(boolean runWithoutTimer) {
		this.runWithoutTimer = runWithoutTimer;
	}
	

	/**
	 * Environment onFinish listener. Called when one iteration of the Environment is done.
	 * 
	 * @author Erik
	 *
	 */
	public interface OnFinishListener {
		public void onFinish(List<IPopulation> popList, List<Obstacle> obsList);
	}
	
}
