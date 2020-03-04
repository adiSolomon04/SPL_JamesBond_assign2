package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        //Start - Reading input json file
        Gson gson = new Gson();
        Reader reader = null;
        try {
            reader = new FileReader(args[0]);
        } catch (FileNotFoundException e) {
        }
        JsonClassRead gsonLoad = gson.fromJson(reader,JsonClassRead.class);
        //Finish - Reading input json file

        //Start - Reading from json class JsonClassRead
        //Build the rannable Classes and create threads
        int numberOfM = gsonLoad.services.M;
        int numberOfMoneypenny = gsonLoad.services.Moneypenny;
        int duration = gsonLoad.services.time;

        M[] m = new M[numberOfM];
        Thread[] ThreadM = new Thread[numberOfM];
        for (int i =0; i<numberOfM; i++){
            m[i]=new M();
            ThreadM[i] = new Thread(m[i]);
        }
        Moneypenny[] moneypenny = new Moneypenny[numberOfMoneypenny];
        Thread[] ThreadMoneypenny = new Thread[numberOfMoneypenny];
        for (int i =0; i<numberOfMoneypenny; i++) {
            moneypenny[i] = new Moneypenny();
            ThreadMoneypenny[i] = new Thread(moneypenny[i]);
        }
        int numberOfIntelligence = gsonLoad.services.intelligence.length;
        Intelligence[] intelligence = new Intelligence[numberOfIntelligence];
        Thread[] ThreadIntelligence = new Thread[numberOfIntelligence];
        for (int i =0; i<numberOfIntelligence; i++) {
            intelligence[i] = new Intelligence(gsonLoad.services.intelligence[i].missions);
            ThreadIntelligence[i] = new Thread(intelligence[i]);
        }
        Q q = new Q();
        Thread ThreadQ = new Thread(q);
        Thread threadTimeservice = new Thread (new TimeService(duration));
        Squad.getInstance().load(gsonLoad.squad);
        Inventory.getInstance().load(gsonLoad.inventory);
        //Finish - creating threads

        //Start - Running Threads
        ThreadQ.start();

        for (Thread ThreM:ThreadM) {
            ThreM.start();
        }
        for (Thread ThreadMoney:ThreadMoneypenny) {
            ThreadMoney.start();
        }
        for (Thread ThreadInte:ThreadIntelligence) {
            ThreadInte.start();
        }
        threadTimeservice.start();
        //Finish - Running Threads


        try {
            ThreadQ.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Thread ThreM:ThreadM) {
            try {
                ThreM.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread ThreadMoney:ThreadMoneypenny) {
            try {
                ThreadMoney.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread ThreadInte:ThreadIntelligence) {
            try {
                ThreadInte.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            threadTimeservice.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Inventory.getInstance().printToFile(args[1]);
        Diary.getInstance().printToFile(args[2]);

    }
    public static class JsonClassRead{
        public String[] inventory;
        public Services services;
        public Agent[] squad;
    }
    public static class Services{
        public int M;
        public int Moneypenny;
        public JsonIntelligence[] intelligence;
        public int time;
    }
    public static class JsonIntelligence{
        public List<MissionInfo> missions;
    }
}