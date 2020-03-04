
package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private static int index = 1;
	private boolean IsSendMessage ;
	private int MoneyPenny;


	public Moneypenny() {
		super("MP"+Integer.toString(index));
		/**
		 * half of the monneyPanny are responsible for the method getAgent
		 * and the other half are for send/release agent
		 */
		IsSendMessage = index%2==1;
		this.MoneyPenny = index;
		index++;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast b) -> {terminate();
			Squad.getInstance().setFlage_terminate(true);
			Squad.getInstance().releaseAgents(Squad.getInstance().getAllAgents());
		/**release all agents to wake up all of the Moneypenny that are waiting to some agent.
		 * it wouldn't cause a problem because in every moneypenny we will have a terminate message
		 * and the missions will be aborted because sendAgent action will fail.
		 */
		});

		if(IsSendMessage){
			//the registration of the send/acquire type of moneyPenny
			subscribeEvent(SendAgentsEvent.class, (SendAgentsEvent e) -> {
				Squad.getInstance().sendAgents(e.getRequestedAgentsToSend(),e.getDuration());
				complete(e, Squad.getInstance().getAgentsNames(e.getRequestedAgentsToSend()));
			});
			subscribeEvent(ReleaseAgentsEvent.class, (ReleaseAgentsEvent e) -> {
				Squad.getInstance().releaseAgents(e.getAgentsToRelease());
			});
		}else {
			//the registration of the getAgents type of moneyPenny
			subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent e) -> {
				if(Squad.getInstance().getAgents(e.getRequestedAgents()))
					complete(e, MoneyPenny);
				else
					complete(e,-1);
			});
		}
	}

}
