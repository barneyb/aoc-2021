package aoc2020.day03_toboggan_trajectory

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    ..##.......
    #...#...#..
    .#....#..#.
    ..#.#...#.#
    .#...##..#.
    ..#.##.....
    .#.#.#....#
    .#........#
    #.##...#...
    #...##....#
    .#..#...#.#
""".trimIndent()

internal class TobogganTrajectoryKtTest {

    @Test
    fun partOne() {
        assertEquals(7, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(336, partTwo(WORKED_EXAMPLE))
    }

    @Test
    fun simple() {
        assertEquals(
            10 * 3 * 2 * 1 * 5, partTwo(
                """
                ...........
                .#.#.#.#...
                .##...#...#
                ...#.....#.
                ..#.#......
                .....#.....
                ...#..#....
                .......#...
                ....#...#..
                .........#.
                .....#....#
                """.trimIndent()
            )
        )
    }

}
