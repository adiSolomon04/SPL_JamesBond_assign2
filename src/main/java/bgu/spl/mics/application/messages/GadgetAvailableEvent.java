package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import javafx.util.Pair;

public class GadgetAvailableEvent implements Event<Pair<Boolean,Integer>> {//19.12
    private String gadget;
    public GadgetAvailableEvent(String gadget) {
        this.gadget = gadget;
    }
    public String getGadget(){
        return gadget;
    }
}
