package aoc2019.day22_slam_shuffle

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class OpTest {

    @Test
    fun negate() {
        val ops = """
            deal into new stack
        """.toOps(10)
        assertAllPositions(ops, listOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 0))
    }

    @Test
    fun addPositive() {
        val ops = """
            cut 3
        """.toOps(10)
        assertAllPositions(ops, listOf(3, 4, 5, 6, 7, 8, 9, 0, 1, 2))
    }

    @Test
    fun addNegative() {
        val ops = """
            cut -4
        """.toOps(10)
        assertAllPositions(ops, listOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5))
    }

    @Test
    fun multiply() {
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
    fun splitNegate() {
        val sep = listOf(
            NewStack(17),
        )
        val comb = listOf(
            Deal(17, 17 - 1),
            Cut(17, 1)
        )
        assertEquals(sep.forward(5), comb.forward(5))
    }

    @Test
    fun combineAddAdd() {
        val sep = listOf(
            Cut(17, 9),
            Cut(17, 14),
        )
        val comb = listOf(
            Cut(17, (9 + 14) % 17),
        )
        assertEquals(sep.forward(5), comb.forward(5))
    }

    // this one seems silly, but it allows clusters of Add and Multiply to form
    @Test
    fun combineAddMultiply() {
        val sep = listOf(
            Cut(17, 9),
            Deal(17, 12),
        )
        val comb = listOf(
            Deal(17, 12),
            Cut(17, (9 * 12) % 17),
        )
        assertEquals(sep.forward(5), comb.forward(5))
    }

// this one is doesn't work (order of operations), but whatever.
//    @Test
//    fun combineMultiplyAdd() {
//        val sep = listOf(
//            Multiply(17, 12),
//            Add(17, 9),
//        )
//        val comb = listOf(
//            Multiply(17, 12),
//            Add(17, 9),
//        )
//        assertEquals(sep.forward(5), comb.forward(5))
//    }

    @Test
    fun combineMultiplyMultiply() {
        val sep = listOf(
            Deal(17, 7),
            Deal(17, 12),
        )
        val comb = listOf(
            Deal(17, (7 * 12) % 17),
        )
        assertEquals(sep.forward(5), comb.forward(5))
    }

    @Test
    fun reduceListOfOps() {
        val sep = """
            deal into new stack
            cut -2
            deal with increment 7
            cut 8
            cut -4
            deal with increment 7
            cut 3
            deal with increment 9
            deal with increment 3
            cut -1
        """.trimIndent()
            .toOps(17)
        val comb = reduceOps(sep)
        assertEquals(sep.forward(5), comb.forward(5))
    }

}
