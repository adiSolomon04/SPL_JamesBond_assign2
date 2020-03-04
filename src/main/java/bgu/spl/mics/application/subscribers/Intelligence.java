package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Message;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionRecivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {//19.12
	private static int i = 1;
	private ConcurrentHashMap<Integer, ConcurrentLinkedQueue<MissionInfo>> ConcurrentHashMapMissionList;

	public Intelligence(List<MissionInfo> ListMissionInfo) {
		super("Intelligence" + i);
		i++;
		ConcurrentHashMapMissionList = new ConcurrentHashMap<>();
		for (MissionInfo Mission : ListMissionInfo) {
			ConcurrentHashMapMissionList.putIfAbsent(Mission.getTimeIssued(), new ConcurrentLinkedQueue<>());
			ConcurrentLinkedQueue<MissionInfo> subscriberConcurrentLinkedQueue = ConcurrentHashMapMissionList.get(Mission.getTimeIssued());
			subscriberConcurrentLinkedQueue.add(Mission);
		}
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast b) -> {
			terminate();
		});
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast e) -> {

			if (ConcurrentHashMapMissionList.containsKey(e.getTimeTick())) {
				ConcurrentLinkedQueue<MissionInfo> missionInfos = ConcurrentHashMapMissionList.get(e.getTimeTick());
				for (MissionInfo Mission : missionInfos) {
					getSimplePublisher().sendEvent(new MissionRecivedEvent(Mission));
				}

			}
		});
	}
}
