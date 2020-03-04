package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.sql.Time;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private int duration;
	private int Tick = 0;


	public TimeService(int duration) {
		super("Time Service");
		this.duration = duration;
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void run() {
		while (Tick < duration) {
			getSimplePublisher().sendBroadcast(new TickBroadcast(Tick));

			try {
				Thread.sleep(100);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Tick++;
		}
		getSimplePublisher().sendBroadcast(new TerminateBroadcast());

	}

}