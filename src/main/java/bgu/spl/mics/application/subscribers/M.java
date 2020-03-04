package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import javafx.util.Pair;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int lastTick = 0;

	private int M;
	private static int index = 1;
	//make sure they are instances. the number of instances will be given in the start
	public M() {
		super("M"+index);
		M = index;
		index++;
	}

	@Override
	protected void initialize() {
		//Be sure to get a Tick broadcast before events
		subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast b) -> {terminate();
		});
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
			this.lastTick = b.getTimeTick();
		});
		subscribeEvent(MissionRecivedEvent.class, (MissionRecivedEvent e) -> {
			boolean executed = false;
			Report newReport = new Report();
			MissionInfo missionInfo = e.getMissionInfo();
			AgentsAvailableEvent AgentEvent = new AgentsAvailableEvent(missionInfo.getSerialAgentsNumbers());
			Future<Integer> AgentEventFuture = getSimplePublisher().sendEvent(AgentEvent);
			/**
			 * This block is running the Checks on
			 * 			Available Agents
			 * 			Gadget Available
			 * 			Time Expired
			 * 	for the current mission executed.
			 */
			if(AgentEventFuture != null &&AgentEventFuture.get()!= null && AgentEventFuture.get()!=-1){
				//will enter if gets a number of the Moneypenny that got all agents
				GadgetAvailableEvent GadgetEvent = new GadgetAvailableEvent(missionInfo.getGadget());
				Future<Pair<Boolean,Integer>> GadgetEventFuture = getSimplePublisher().sendEvent(GadgetEvent);
				if(GadgetEventFuture!= null && GadgetEventFuture.get()!=null && GadgetEventFuture.get().getKey()){
					//will check availability of gadget
					if(GadgetEventFuture.get().getValue()<missionInfo.getTimeExpired()){
						//accomplished All
						SendAgentsEvent SendAgentsEvent = new SendAgentsEvent(missionInfo.getSerialAgentsNumbers(),missionInfo.getDuration());
						Future<List<String>> AgentNamesFuture = getSimplePublisher().sendEvent(SendAgentsEvent);
						if(AgentNamesFuture !=null && AgentNamesFuture.get() != null) {
							newReport.setAgentsNames(AgentNamesFuture.get());
							newReport.setQTime(GadgetEventFuture.get().getValue());
							executed = true;
						}
					}
					else{//aborted - time Expired
						//release all agents that were acquired
						ReleaseAgentsEvent ReleaseEvent = new ReleaseAgentsEvent(missionInfo.getSerialAgentsNumbers());
						getSimplePublisher().sendEvent(ReleaseEvent);
					}
				}
				else{//aborted - gadget not found
					//release all agents that were acquired
					ReleaseAgentsEvent ReleaseEvent = new ReleaseAgentsEvent(missionInfo.getSerialAgentsNumbers());
					getSimplePublisher().sendEvent(ReleaseEvent);
				}
			}
			complete(e, executed);
			Diary.getInstance().incrementTotal();
			//make the new report if the mission was successfully executed
			if(executed) {
				setNewReport(newReport, missionInfo, AgentEventFuture);
			}
			//end report Making
		});
	}

	/**
	 * configures the new report if the mission as
	 * successful
	 * @param newReport the new Report
	 * @param missionInfo the current mission info
	 * @param AgentEventFuture the number of the moneypenny that got
	 *                        agents in this mission
	 */
	private void setNewReport(Report newReport, MissionInfo missionInfo, Future<Integer> AgentEventFuture){
		newReport.setMissionName(missionInfo.getMissionName());
		newReport.setM(M);
		newReport.setMoneypenny(AgentEventFuture.get());
		newReport.setAgentsSerialNumbers(missionInfo.getSerialAgentsNumbers());
		newReport.setGadgetName(missionInfo.getGadget());
		newReport.setTimeIssued(missionInfo.getTimeIssued());
		newReport.setTimeCreated(lastTick);
		Diary.getInstance().addReport(newReport);
	}

}
