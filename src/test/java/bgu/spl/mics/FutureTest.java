package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<Integer> num;
    @BeforeEach
    public void setUp(){
        num = new Future<Integer>();
    }

    @Test
    public void test(){

        assertFalse(num.isDone());
        assertTrue(num.get(2, TimeUnit.MICROSECONDS)==null);
        num.resolve(2);
        assertTrue(num.get()==2);
        assertTrue(num.isDone());
        assertFalse(num.get(2, TimeUnit.MICROSECONDS)==null);

        //fail("Not a good test");
    }
}
