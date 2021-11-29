package aoc2019.intcode

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class VMTest {

    @Test
    fun stepping() {
        val vm = VM("1,9,10,3,2,3,11,0,99,30,40,50")
        assertEquals(1, vm[0])
        assertEquals(3, vm[3])
        vm.step()
        assertEquals(1, vm[0])
        assertEquals(70, vm[3])
        vm.step()
        assertEquals(3500, vm[0])
        assertEquals(70, vm[3])
    }

}
