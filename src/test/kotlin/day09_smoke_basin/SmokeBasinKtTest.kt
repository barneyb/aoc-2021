package day09_smoke_basin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
2199943210
3987894921
9856789892
8767896789
9899965678
""".trimIndent()

internal class SmokeBasinKtTest {

    @Test
    fun partOne() {
        assertEquals(
            1, partOne(
                """
            011
            111
            111
        """.trimIndent()
            )
        )
        assertEquals(
            1, partOne(
                """
            110
            111
            111
        """.trimIndent()
            )
        )
        assertEquals(
            1, partOne(
                """
            111
            111
            110
        """.trimIndent()
            )
        )
        assertEquals(
            1, partOne(
                """
            111
            111
            011
        """.trimIndent()
            )
        )
        assertEquals(
            4, partOne(
                """
            010
            111
            010
        """.trimIndent()
            )
        )
        assertEquals(
            1, partOne(
                """
            101
            111
            111
        """.trimIndent()
            )
        )
        assertEquals(
            1, partOne(
                """
            111
            110
            111
        """.trimIndent()
            )
        )
        assertEquals(
            1, partOne(
                """
            111
            111
            101
        """.trimIndent()
            )
        )
        assertEquals(
            1, partOne(
                """
            111
            011
            111
        """.trimIndent()
            )
        )
        assertEquals(
            4, partOne(
                """
            101
            010
            101
        """.trimIndent()
            )
        )
        assertEquals(15, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(1134, partTwo(WORKED_EXAMPLE))
    }

}
