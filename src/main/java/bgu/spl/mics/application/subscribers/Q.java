package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import javafx.util.Pair;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {//19.12
	private static int index = 1;
	private int lastTick = 0;
	public Q() {
		super("Q"+Integer.toString(index));
		index++;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast b) -> {
			terminate();
		});
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast e) -> {
			this.lastTick = e.getTimeTick();
		});
		subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent e) -> {
			complete(e, new Pair<>(Inventory.getInstance().getItem(e.getGadget()),lastTick));//e Has the gadget?
	});
	}
}
