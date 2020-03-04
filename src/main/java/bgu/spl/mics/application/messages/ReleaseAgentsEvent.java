package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

import java.util.List;
public class ReleaseAgentsEvent implements Event<Boolean> {
    private List<String> agents;
    public ReleaseAgentsEvent(List<String> agents){
        this.agents = agents;
    }
    public List<String> getAgentsToRelease(){
        return agents;
    }
}