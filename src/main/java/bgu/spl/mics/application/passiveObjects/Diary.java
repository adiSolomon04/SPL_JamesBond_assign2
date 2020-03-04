package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MessageBrokerImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
//Synchronized all
public class Diary {
	private static class DiaryHolder{
		private static Diary instance = new Diary();
	}
	private List<Report> reports = new LinkedList<>();
	private int total = 0;
	/**
	 * Retrieves the single instance of this class.
	 */

	public static synchronized Diary getInstance() {
		return DiaryHolder.instance;
	}

	public synchronized List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public synchronized void addReport(Report reportToAdd){
		reports.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public synchronized void printToFile(String filename){
		Gson gson =new GsonBuilder().setPrettyPrinting().create();// new Gson();
		String s = gson.toJson(this);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public synchronized int getTotal(){
		return reports.size();
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public synchronized void incrementTotal(){
		total++;
	}


}
