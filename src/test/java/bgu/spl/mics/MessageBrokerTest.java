package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.subscribers.ExampleBroadcastSubscriber;
import bgu.spl.mics.example.subscribers.ExampleEventHandlerSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    Broadcast TestBroadcast;
    Event<String> TestEvent;
    Subscriber TestBroadcastSubscriber;
    Subscriber TestEventSubscriber;
    MessageBroker TestMessageBroker;
    @BeforeEach
    public void setUp(){
        TestMessageBroker = MessageBrokerImpl.getInstance();
        TestBroadcastSubscriber = new ExampleBroadcastSubscriber("name1", new String[]{"6"});///what about this
        TestEventSubscriber = new ExampleEventHandlerSubscriber("name1", new String[]{"7"});
        TestEvent = new ExampleEvent("1");
        TestBroadcast= new ExampleBroadcast("2");




    }

    @Test
    public void test(){
        TestMessageBroker.register(TestBroadcastSubscriber);
        TestMessageBroker.register(TestEventSubscriber);
        TestMessageBroker.subscribeBroadcast(TestBroadcast.getClass(), TestBroadcastSubscriber);
        TestMessageBroker.subscribeEvent(ExampleEvent.class, TestEventSubscriber);
        TestMessageBroker.sendBroadcast(TestBroadcast);
        try {
            Message m = TestMessageBroker.awaitMessage(TestBroadcastSubscriber);
            assertSame(m,TestBroadcast);
        } catch (InterruptedException e) {
            fail();
        }

        Future<String>  F =TestMessageBroker.sendEvent(TestEvent);
        TestMessageBroker.complete(TestEvent,"aa");
        assertTrue(F.isDone());
        assertTrue(F.get()=="aa");
        TestMessageBroker.unregister(TestBroadcastSubscriber);
        try {
            Message m = TestMessageBroker.awaitMessage(TestBroadcastSubscriber);
            fail();

        } catch (InterruptedException e) {

        }
        //fail("Not a good test");
    }
}
