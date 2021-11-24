package aoc2015.day06_fire_hazard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortArrayTest {

    @Test
    public void primitive() {
        short[] arr = new short[10];
        arr[0] += 1;
        arr[0] += 1;
        assertEquals(2, arr[0]);
    }

    @Test
    public void boxed() {
        Short[] arr = new Short[10];
        arr[0] = 0; // have to initialize
        arr[0] = (short) (arr[0] + 1);
        arr[0] = (short) (arr[0] + 1);
        assertEquals((short) 2, arr[0]);
    }

}
