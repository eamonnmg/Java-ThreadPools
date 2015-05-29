package ie.lyit.threadpoolapp;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
 
/*
 * This class is a modified version of a code example found
 * online(Kumar, 2013)
 * It is an implementation of the RejectedExecutionHandler interface. 
 * By passing this object as a parameter in a ThreadPoolExecutor
 * object, attempts to exceed the bounds of the task queue can be handled gracefully.
 */

public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
	private GUI gui;
	/**
	 * 
	 * @param gui - takes in the gui class so it can output to a Jdialog box
	 */
	public RejectedExecutionHandlerImpl(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		gui.dialog("Task Rejected. Perhaps the server is overloaded. Please wait and try again.");
	}

}