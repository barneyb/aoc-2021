package aoc2015.day05_intern_elves

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class InternElvesKtTest {

    @Test
    fun partOne() {
        assertEquals(
            2, partOne(
                """
                ugknbfddgicrmopn
                aaa
                jchzalrnumimnmhp
                haegwjzuvuyypxyu
                dvszwmarrgswjxmb
                """.trimIndent()
            )
        )
    }

    @Test
    fun partTwo() {
        assertEquals(
            2, partTwo(
                """
                qjhvhtzxzqqjkmpb
                xxyxx
                uurcxstgmygtbstg
                ieodomkazucvgmuy
                """.trimIndent()
            )
        )
    }

}
