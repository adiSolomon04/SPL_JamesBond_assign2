package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionRecivedEvent implements Event<Boolean> {//19.12
    private MissionInfo missionInfo;
    public MissionRecivedEvent(MissionInfo missionInfo){
        this.missionInfo = missionInfo;
    }
    public MissionInfo getMissionInfo(){
        return missionInfo;
    }

}
