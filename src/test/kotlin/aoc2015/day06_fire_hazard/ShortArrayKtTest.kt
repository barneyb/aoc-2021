package aoc2015.day06_fire_hazard

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShortArrayKtTest {

    @Test
    fun primitive() {
        val arr = ShortArray(10)
        arr[0] = (arr[0] + 1).toShort()
        arr[0] = (arr[0] + 1).toShort()
        assertEquals(2, arr[0])
    }

    @Test
    fun boxed() {
        val arr = arrayOfNulls<Short>(10)
        arr[0] = 0 // have to initialize
        arr[0] = (arr[0]!! + 1).toShort()
        arr[0] = (arr[0]!! + 1).toShort()
        assertEquals(2.toShort(), arr[0])
    }

}
