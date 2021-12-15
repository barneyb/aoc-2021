package day14_extended_polymerization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    NNCB

    CH -> B
    HH -> N
    CB -> H
    NH -> C
    HB -> C
    HC -> B
    HN -> C
    NN -> C
    BH -> H
    NC -> B
    NB -> B
    BN -> B
    BB -> N
    BC -> B
    CC -> N
    CN -> C
""".trimIndent()

internal class ExtendedPolymerizationKtTest {

    @Test
    fun solve0() {
        assertEquals(1, solve(WORKED_EXAMPLE, 0))
    }

    @Test
    fun solve1() {
        assertEquals(1, solve(WORKED_EXAMPLE, 1))
    }

    @Test
    fun solve2() {
        assertEquals(5, solve(WORKED_EXAMPLE, 2))
    }

    @Test
    fun solve3() {
        assertEquals(7, solve(WORKED_EXAMPLE, 3))
    }

    @Test
    fun partOne() {
        assertEquals(1588, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(2188189693529, partTwo(WORKED_EXAMPLE))
    }

}
