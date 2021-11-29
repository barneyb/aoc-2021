package aoc2019.day02_program_alarm

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ProgramAlarmKtTest {

    @Test
    fun partOne() {
        assertEquals(
            70,
            evaluateForPosition("1,9,10,3,2,3,11,0,99,30,40,50", 3)
        )
        assertEquals(
            3500,
            evaluateForPosition("1,9,10,3,2,3,11,0,99,30,40,50", 0)
        )
        assertEquals(2, evaluateForPosition("1,0,0,0,99", 0))
        assertEquals(6, evaluateForPosition("2,3,0,3,99", 3))
        assertEquals(9801, evaluateForPosition("2,4,4,5,99,0", 5))
        assertEquals(30, evaluateForPosition("1,1,1,4,99,5,6,0,99", 0))
    }

}
