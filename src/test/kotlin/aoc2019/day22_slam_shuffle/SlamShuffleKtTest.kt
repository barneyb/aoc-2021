package aoc2019.day22_slam_shuffle

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SlamShuffleKtTest {

    @Test
    fun reverse() {
        val ops = """
            deal into new stack
        """.toOps(10)
        assertAllPositions(ops, listOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 0))
    }

    @Test
    fun cutPositive() {
        val ops = """
            cut 3
        """.toOps(10)
        assertAllPositions(ops, listOf(3, 4, 5, 6, 7, 8, 9, 0, 1, 2))
    }

    @Test
    fun cutNegative() {
        val ops = """
            cut -4
        """.toOps(10)
        assertAllPositions(ops, listOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5))
    }

    @Test
    fun newStack() {
        val ops = """
            deal with increment 3
        """.toOps(10)
        assertEquals(0, ops.forward(0))
        assertEquals(1, ops.forward(7))
        assertEquals(2, ops.forward(4))
        assertAllPositions(ops, listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3))
    }

    private fun assertAllPositions(ops: List<Op>, results: List<Long>) {
        for (i in results.indices) {
            assertEquals(
                i.toLong(),
                ops.forward(results[i]),
                "Expected ${results[i]} to end up at $i"
            )
        }
    }

    @Test
    fun itsARing() {
        val input = """
                cut 8
                deal with increment 5
                cut -5
                deal with increment 2
                deal into new stack
                cut 1
                deal with increment 3
                cut -2
            """
//        val c = 9L
//        assertEquals(
//            partOne(input, 11, c, 1),
//            (((((((c
//                    - 8 + 11)     % 11 // cut 8
//                    * 5)          % 11 // deal with increment 5
//                    + 5)          % 11 // cut -5
//                    * 2)          % 11 // deal with increment 2
//                    * -1 + 11 - 1      // deal into new stack
//                    - 1 + 11)     % 11 // cut 1
//                    * 3)          % 11 // deal with increment 3
//                    + 2)          % 11 // cut -2
//        )

        var forward = partOne(input, 11, 9, 5)
        var reverse = partTwo(input, 11, 9, 11L - 5 - 1)
        assertEquals(forward, reverse)

        forward = partTwo(input, 11, 2, 7)
        reverse = partOne(input, 11, 2, 11L - 7 - 1)
        assertEquals(forward, reverse)
    }

    @Test
    fun itsARingForReal() {
        val input = util.getInput(javaClass)
        val forward = partOne(input, DECK_SIZE_ONE, 2019, 6789)
        val reverse =
            partTwo(input, DECK_SIZE_ONE, 2019, DECK_SIZE_ONE - 6789 - 1)
        assertEquals(forward, reverse)
    }

    @Test
    fun smallPartTwo() {
        val input = util.getInput(javaClass)
        // deck size 10007
        // iterations 9787
        // what card ends up in position 2020?
        val result = partTwo(input, DECK_SIZE_ONE, 2020, 9787)
        assertEquals(2295, result)
        // validate that card 2295, after 9787 iterations, is in slot 2020
        assertEquals(2020, partOne(input, DECK_SIZE_ONE, result, 9787))
    }

}
