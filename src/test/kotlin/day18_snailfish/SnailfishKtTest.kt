package day18_snailfish

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
    [[[5,[2,8]],4],[5,[[9,9],0]]]
    [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
    [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
    [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
    [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
    [[[[5,4],[7,7]],8],[[8,3],8]]
    [[9,3],[[9,9],[6,[4,9]]]]
    [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
    [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
""".trimIndent()

private val SOME_EXAMPLE_NUMBERS = """
    [1,2]
    [[1,2],3]
    [9,[8,7]]
    [[1,9],[8,5]]
    [[[[1,2],[3,4]],[[5,6],[7,8]]],9]
    [[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]
    [[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]
""".trimIndent()

private val LARGER_EXAMPLE = """
    [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
    [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
    [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
    [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
    [7,[5,[[3,8],[1,4]]]]
    [[2,[2,2]],[8,[8,1]]]
    [2,9]
    [1,[[[9,3],9],[[9,0],[0,7]]]]
    [[[5,[7,4]],7],1]
    [[[[4,2],2],6],[8,7]]
""".trimIndent()

fun String.snailExplode(): String {
    val num = toSnailNum()
    num.explode()
    return num.toString()
}

fun String.snailSplit(): String {
    val num = toSnailNum()
    num.split()
    return num.toString()
}

fun String.snailReduce(): String {
    val num = toSnailNum()
    num.reduce()
    return num.toString()
}

fun String.snailPlus(other: String) =
    toSnailNum().plus(other.toSnailNum()).toString()

val String.snailMagnitude: Int
    get() = toSnailNum().magnitude

internal class SnailfishKtTest {

    @Test
    fun explodeLeft() {
        assertEquals(
            "[[[[0,9],2],3],4]",
            "[[[[[9,8],1],2],3],4]".snailExplode()
        )
    }

    @Test
    fun explodeRight() {
        assertEquals(
            "[7,[6,[5,[7,0]]]]",
            "[7,[6,[5,[4,[3,2]]]]]".snailExplode()
        )
    }

    @Test
    fun explodeMiddle() {
        assertEquals(
            "[[6,[5,[7,0]]],3]",
            "[[6,[5,[4,[3,2]]]],1]".snailExplode()
        )
        assertEquals(
            "[[6,[5,[13,0]]],10]",
            "[[6,[5,[4,[9,9]]]],1]".snailExplode()
        )
    }

    @Test
    fun explodeMultiple() {
        assertEquals(
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
            "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".snailExplode()
        )
        assertEquals(
            "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]".snailExplode()
        )
    }

    @Test
    fun explodeLargerStep() {
        assertEquals(
            "[[[[12,12],[6,14]],[[15,0],[25,0]]],[3,9]]",
            "[[[[12,12],[6,14]],[[15,0],[17,[8,1]]]],[2,9]]".snailExplode()
        )
    }

    @Test
    fun split() {
        assertEquals("9", "9".snailSplit())
        assertEquals("[5,6]", "11".snailSplit())
        assertEquals("[[5,5],11]", "[10,11]".snailSplit())
    }

    @Test
    fun reduce() {
        assertEquals(
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
            "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]".snailReduce()
        )
    }

    @Test
    fun reduceLargerStep() {
        assertEquals(
            "[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]",
            "[[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]],[2,9]]".snailReduce()
        )
    }

    private fun exampleTest(expected: String, input: String) {
        assertEquals(
            expected,
            input
                .lines()
                .reduce(String::snailPlus)
                .snailReduce()
        )
    }

    @Test
    fun exampleOne() {
        exampleTest(
            "[[[[1,1],[2,2]],[3,3]],[4,4]]",
            """
                [1,1]
                [2,2]
                [3,3]
                [4,4]
            """.trimIndent()
        )
    }

    @Test
    fun exampleTwo() {
        exampleTest(
            "[[[[3,0],[5,3]],[4,4]],[5,5]]",
            """
                [1,1]
                [2,2]
                [3,3]
                [4,4]
                [5,5]
            """.trimIndent()
        )
    }

    @Test
    fun exampleThree() {
        exampleTest(
            "[[[[5,0],[7,4]],[5,5]],[6,6]]",
            """
                [1,1]
                [2,2]
                [3,3]
                [4,4]
                [5,5]
                [6,6]
            """.trimIndent()
        )
    }

    @Test
    fun exampleLarger() {
        exampleTest(
            "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]",
            LARGER_EXAMPLE
        )
    }

    @Test
    fun magnitude() {
        assertEquals(29, "[9,1]".snailMagnitude)
        assertEquals(21, "[1,9]".snailMagnitude)
        assertEquals(129, "[[9,1],[1,9]]".snailMagnitude)

        assertEquals(143, "[[1,2],[[3,4],5]]".snailMagnitude)
        assertEquals(
            1384,
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]".snailMagnitude
        )
        assertEquals(445, "[[[[1,1],[2,2]],[3,3]],[4,4]]".snailMagnitude)
        assertEquals(791, "[[[[3,0],[5,3]],[4,4]],[5,5]]".snailMagnitude)
        assertEquals(1137, "[[[[5,0],[7,4]],[5,5]],[6,6]]".snailMagnitude)
        assertEquals(
            3488,
            "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]".snailMagnitude
        )

    }

    @Test
    fun partOne() {
        assertEquals(4140, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(3993, partTwo(WORKED_EXAMPLE))
    }

}
