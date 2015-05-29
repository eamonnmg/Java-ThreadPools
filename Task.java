package ie.lyit.threadpoolapp;

import java.util.Random;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * 
 * @author l00093050
 *This is a task object. It implement runnable and will be passed into 
 *the thread pool. Each new task has a unique auto id 
 *
 */
public class Task implements Runnable {
	private int id;
	private static int nextId = 0;
	private long time;
	private GUI gui;
	
	
	public Task(){
		this.id = nextId++;
		this.time = (long)new Random().nextInt(1000);
	}
	
	public Task(long time,GUI gui){
		this.id = nextId++;
		this.time = time;
		this.gui = gui;
	}
	
	/**
	 * This is the code that runs in each thread
	 */
	@Override
	public void run() {
		
		// get current time, this will be appended to the log.txt file.
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		long start = System.currentTimeMillis();
		try {
			Thread.sleep(this.time);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		
		
		//print to log text file
		try {
			gui.printToLog("Task id:"+id+"\t Time Taken:"+(end-start)+"\t Time:"+sdf.format(cal.getTime()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
}
