package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.Event;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {


	private static class SquadHolder {
		private static Squad instance = new Squad();
	}
	private Map<String, Agent> agents = new ConcurrentHashMap<>();



	private boolean flag_terminate =false;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SquadHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent a:agents) {
			this.agents.put(a.getSerialNumber(),a);

		}

	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (String s : serials) {
			synchronized (agents.get(s)) {
				agents.get(s).release();
				agents.get(s).notifyAll();
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		//sleep for 100*time

		try {
			Thread.sleep(100*time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//releaseAgents
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) {

		serials.sort(Comparator.comparing(String::toString));

		for (String s : serials) {
			if (!agents.containsKey(s))
				return false;
		}
		for (String s : serials) {
			try {
				synchronized (agents.get(s)) {// need to release in release
					while (!agents.get(s).isAvailable()&&!flag_terminate)
						agents.get(s).wait();
					agents.get(s).acquire();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		return true;
	}

	/**
	 * gets the agents names
	 * @param serials the serial numbers of the agents
	 * @return a list of the names of the agents with the specified serials.
	 */
	public List<String> getAgentsNames(List<String> serials){
		List<String> agentsNames = new LinkedList<>();
		for (String s : serials) {
			agentsNames.add(agents.get(s).getName());
		}
		return agentsNames;
	}

	/**
	 * get all of the agents
	 * @return a list of the agents that exist in the Squad
	 */

	public List<String> getAllAgents() {
		List<String> serials = new LinkedList<>();
		for (String s:agents.keySet()) {
			serials.add(s);
		}
		return serials;
		//releaseAgents(serials);
	}
	public void setFlage_terminate(boolean flage_terminate) {
		this.flag_terminate = flage_terminate;
	}

}