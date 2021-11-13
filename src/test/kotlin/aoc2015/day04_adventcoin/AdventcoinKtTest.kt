package aoc2015.day04_adventcoin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AdventcoinKtTest {

    @Test
    fun partOne() {
        assertEquals(609043, partOne("abcdef"))
        assertEquals(1048970, partOne("pqrstuv"))
    }

}

