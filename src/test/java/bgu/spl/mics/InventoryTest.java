package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inv;
    @BeforeEach
    public void setUp(){
        Inventory inv = Inventory.getInstance();
    }

    @Test
    public void test(){
        String[] gadgetIn = {"a", "b", "c"};
        String[] gadgetNotIn = {"d", "e", "f"};
        inv.load(gadgetIn);
        //these gadget suppose to be in
        for (int i = 0; i < gadgetIn.length; i++) {
            assertTrue(inv.getItem(gadgetIn[i])) ;
        }
        //these gadget do not suppose to be in (never entered)
        for (int i = 0; i < gadgetNotIn.length; i++) {
            assertFalse(inv.getItem(gadgetNotIn[i])); ;
        }
        //these gadget do not suppose to be in (we done get already suppose to get out)
        for (int i = 0; i < gadgetIn.length; i++) {
            assertFalse(inv.getItem(gadgetIn[i])); ;
        }
    }
}
