package ie.lyit.threadpoolapp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Eamonn
 *This is a tester class to ensure thread safety.
 */
public class Tester {

	public static void main(String[] args) {
		GUI gui = new GUI();
		//threadpool with poolsize of 5 and no queue limit
		ThreadPool tp = new ThreadPool(5);
		ThreadPoolExecutor ex = tp.getExecutor();
		//add 20000 tasks to the thread pool
		//each task will call a the printToLog() method in the GUI class
		//this method increments a static int
		//by the end of the loop the counter should = 20000
		for(int i =0; i<20000;i++){
			ex.submit(new Task(0,gui));
		}
		//shutdown the threadpool executor
		ex.shutdown();
		//wait until active threads a finished executing
		try {
			ex.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Number of times written to file: "+gui.getWriteCount());
		
		
		
		
		
	}

}
