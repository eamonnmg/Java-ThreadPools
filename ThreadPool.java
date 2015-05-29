package ie.lyit.threadpoolapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class serves as a simpler means of implementing Java's ThreadPoolExecutor Class
 * to create a threadpool. 
 * @param poolSize - number of threads in the pool
 * @param maxQueueSize - max number of tasks that can be queue, waiting to get into threadpool
 * @param gui - the gui must be passed in so the rejection handle can output its alert to a JDialog box
 */
public class ThreadPool {

	private int poolSize;
	private int maxQueueSize;
	private ThreadPoolExecutor executor;
	private GUI gui;
	/**
	 * Note: This constructor is for testing purposes
	 * @param poolSize - number of worker threads
	 */
	public ThreadPool(int poolSize) {
		this.poolSize = poolSize;
		this.executor = new ThreadPoolExecutor(poolSize, poolSize, 10,
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	/**
	 * Note: This constructor is for testing purposes
	 * @param poolSize - number of worker threads
	 * @param maxPoolSize - max number of threads that can be waiting in the queue to get into the thread pool
	 */
	public ThreadPool(int poolSize, int maxPoolSize) {
		this.poolSize = poolSize;
		this.maxQueueSize = maxPoolSize;
		this.executor = new ThreadPoolExecutor(poolSize, poolSize, 10,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(maxQueueSize));
	}
	/**
	 * Note: This constructor is called in the actual GUI Application
	 * @param poolSize - number of worker threads
	 * @param maxPoolSize - max number of threads that can be waiting in the queue to get into the thread pool
	 * @param gui - passes the gui object into the RejectedExecutionHandlerImpl
	 * This facilitates printing rejection alerts out to a JDialog Box 
	 */
	public ThreadPool(int poolSize, int maxPoolSize,GUI gui) {
		this.poolSize = poolSize;
		this.maxQueueSize = maxPoolSize;
		this.gui = gui;
		this.executor = new ThreadPoolExecutor(poolSize, poolSize, 10,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(maxQueueSize),new RejectedExecutionHandlerImpl(gui));
	}
	
	/**
	 * This method returns the executor method defined by the constructor above
	 * Tasks are passed into the executor to be processed
	 * @return
	 */
	public ThreadPoolExecutor getExecutor(){
		return this.executor;
	}
	

}

