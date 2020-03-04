package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {//19.12
    private int timeTick;
    public TickBroadcast(int timeTick){
        this.timeTick = timeTick;
    }
    public int getTimeTick(){
        return timeTick;
    }
}
