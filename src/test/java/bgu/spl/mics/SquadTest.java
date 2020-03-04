package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    private Squad S;
    @BeforeEach
    public void setUp(){
        S =Squad.getInstance();
    }

    @Test
    public void test(){
        Agent[] agent = new Agent[5];
        String[] name = {"a","b","c","d","e"};
        List<String> nameList= new LinkedList();//not thread safe
        List<String> serials = new LinkedList();//not thread safe
        for (int i = 0; i <agent.length ; i++) {
            agent[i] = new Agent();
            agent[i] .setSerialNumber("00"+i);
            serials.add("00"+i);
            agent[i].setName(name[i]);
            nameList.add(name[i]);
        }
        S.load(agent);
        assertTrue(S.getAgents(serials));//check that all the serials we enter in the squad
        S.releaseAgents(serials);
        List<String> nameFromS = S.getAgentsNames(serials);
        assertTrue(nameList.containsAll(nameFromS));//check if all the names we enter in the squad


        String newName = "007";
        serials.add(newName);
        assertFalse(S.getAgents(serials)); //check that do not return true if not all the serials in the squad

    }
}
