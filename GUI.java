package ie.lyit.threadpoolapp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * 
 * @author l00093050
 * This class creates the GUI and contains a method to print to a file.
 * It also contains a number of methods that allow 
 * other classes to access gui conponents.
 *  
 *
 */
public class GUI extends JFrame {
	private int poolSize;
	private int maxQueue;
	private ThreadPool tp;
	private ThreadPoolExecutor executor;
	private static int writeCount = 0;
	
	
	//windowbuider gui components
	private JPanel contentPane;
	private JTextField txtPoolSize;
	private JTextField txtTaskLength;
	private JButton btnSubmitTask;
	private JLabel lblPoolSize_1;
	private JLabel lblActiveThreads;
	private JLabel lblQueuedthreads;
	private JLabel lblCompletedTasks;
	public JTextArea textAreaResults;
	private JScrollPane scrollPane;
	private JButton btnShutdown;
	private JTextField txtMaxQueue;
	private JButton btnInitialise;


	//https://www.youtube.com/watch?v=v-qCbMRkk-s
	/**
	 * 
	 * @param toLog - string of text to be appended to txt file
	 * This code repurposes some code the youtube tutorial
	 * "Java Advanced Programming Tutorial 11 How To Append To A File"(2013)
	 * @throws IOException
	 */
	public synchronized void printToLog(String toLog) throws IOException{
		File log = new File("log.txt");
		FileWriter fileWriter = new FileWriter(log,true);
		BufferedWriter br = new BufferedWriter(fileWriter);
		PrintWriter printWriter = new PrintWriter(br);
		
		
		if(!log.exists()){
			log.createNewFile();
		}
		
		printWriter.print(toLog+"\n");
		//append string to textarea. add line break for formatiing
		textAreaResults.append(toLog+"\n");
		
		//count write - concurency control
		writeCount++;
		printWriter.close();
		
	}
	public int getWriteCount(){
		return writeCount;
	}
	public void dialog(String dialog){
		JOptionPane.showMessageDialog(this, dialog);
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 628, 431);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Initialise Thread Pool", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblPoolSize = new JLabel("Pool Size");
		panel_1.add(lblPoolSize);
		
		txtPoolSize = new JTextField();
		panel_1.add(txtPoolSize);
		txtPoolSize.setColumns(10);
		
		btnInitialise = new JButton("Initialise");
		btnInitialise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//text box validation
				try {
					poolSize = Integer.parseInt(txtPoolSize.getText());
					maxQueue = Integer.parseInt(txtMaxQueue.getText());
					tp = new ThreadPool(poolSize,maxQueue,GUI.this);
					executor = tp.getExecutor();
				
				
				
				
				
					//start threadpool monitor in new thread
					Thread monitor = new Thread(new Runnable() {
						
						@Override
						public void run() {
							while(true){
								lblPoolSize_1.setText("Pool Size:"+executor.getCorePoolSize());
								lblActiveThreads.setText("Active Threads: "+executor.getActiveCount());
								//task count - completed tasks will give tasks to be done
								//the queued tasks = total tasks submitted - (completet tasks + tasks currently in the pool) ie tasks waiting to enter pool
								lblQueuedthreads.setText("Queued Tasks: "+(executor.getTaskCount()-(executor.getCompletedTaskCount()+executor.getActiveCount()))+"/"+maxQueue);
								lblCompletedTasks.setText("Completed Tasks: "+executor.getCompletedTaskCount());
							}
							
						}
					});
					monitor.start();
					
					//print to log new threadpool started
					
					printToLog("Thread pool Initialised with pool size of "+executor.getCorePoolSize());//core and maximum are same in fixed thread pool
					
					
					
					//enable submit button now that every thing is ready to go
					btnSubmitTask.setEnabled(true);
					btnShutdown.setEnabled(true);
					btnInitialise.setEnabled(false);
				} catch (Exception e) {
					dialog("Enter Both Thread Pool size and max que size!");
				}
			}
		});
		
		JLabel lblMaxQueue = new JLabel("Max Queue");
		panel_1.add(lblMaxQueue);
		
		txtMaxQueue = new JTextField();
		panel_1.add(txtMaxQueue);
		txtMaxQueue.setColumns(10);
		panel_1.add(btnInitialise);
		
		btnShutdown = new JButton("Shutdown");
		btnShutdown.setEnabled(false);
		btnShutdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executor.shutdown();
				//wait for remaining threads to process(up to a minute)
				try {
					executor.awaitTermination(1, TimeUnit.MINUTES);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					printToLog("Thread pool shutdown. all tasks processed.");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				//enable submit button now that every thing is ready to go
				btnSubmitTask.setEnabled(false);
				btnShutdown.setEnabled(false);
				btnInitialise.setEnabled(true);
			}
		});
		panel_1.add(btnShutdown);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Thread Pool Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		lblPoolSize_1 = new JLabel("Pool Size: ");
		panel_2.add(lblPoolSize_1);
		
		
		lblActiveThreads = new JLabel("Active Threads:");
		panel_2.add(lblActiveThreads);
		
		lblQueuedthreads = new JLabel("QueuedThreads:");
		panel_2.add(lblQueuedthreads);
		
		lblCompletedTasks = new JLabel("Completed Tasks:");
		panel_2.add(lblCompletedTasks);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Task", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_3);
		
		JLabel lblTaskLengthmilis = new JLabel("Task length(Milis)");
		panel_3.add(lblTaskLengthmilis);
		
		txtTaskLength = new JTextField();
		panel_3.add(txtTaskLength);
		txtTaskLength.setColumns(10);
		
		btnSubmitTask = new JButton("Submit Task to Server  ");
		btnSubmitTask.setEnabled(false);
		btnSubmitTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//make sure time field is filled
				try {
					long time = Long.parseLong(txtTaskLength.getText());
					executor.submit(new Task(time,GUI.this));
				} catch (Exception e) {
					dialog("Enter trask Time!");
				}
				
				
				
			}
		});
		contentPane.add(btnSubmitTask, BorderLayout.CENTER);
		
		textAreaResults = new JTextArea();
		textAreaResults.setRows(8);
		textAreaResults.setEditable(false);
		
		scrollPane = new JScrollPane(textAreaResults);
		contentPane.add(scrollPane, BorderLayout.SOUTH);
		
		
	}
	
	
}


