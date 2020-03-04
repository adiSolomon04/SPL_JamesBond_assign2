package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

import java.util.List;
public class SendAgentsEvent implements Event<List<String>> {
    private List<String> agents;
    private int time;
    public SendAgentsEvent(List<String> agents,int time){
        this.agents = agents;
        this.time = time;
    }
    public List<String> getRequestedAgentsToSend(){
        return agents;
    }
    public int getDuration(){
        return time;
    }

}