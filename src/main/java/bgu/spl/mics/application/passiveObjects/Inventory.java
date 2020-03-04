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
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private static class InventoryHolder {
		private static Inventory instance = new Inventory();
	}
	private List<String> gadgets = new LinkedList<>();
	/**
     * Retrieves the single instance of this class.
     */
	public static synchronized Inventory getInstance() {
		return InventoryHolder.instance;
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public synchronized void load (String[] inventory) {
		for (String I:inventory) {
			gadgets.add(I);
		}
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public synchronized boolean getItem(String gadget){
		return gadgets.remove(gadget);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the of the gadgeds.
	 * This method is called by the main method in order to generate the output.
	 */
	public synchronized void printToFile(String filename){
		Gson gson =new GsonBuilder().setPrettyPrinting().create();
		String s = gson.toJson(this.gadgets);
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
}
