package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent implements Event<Integer> {//19.12
    private List<String> agents;
    private  int Moneypenny;
    public AgentsAvailableEvent(List<String> agents){
        this.agents = agents;
    }
    public List<String> getRequestedAgents(){
        return agents;
    }
    public void setMoneypenny(int Moneypenny){
        this.Moneypenny = Moneypenny;
    }
}
