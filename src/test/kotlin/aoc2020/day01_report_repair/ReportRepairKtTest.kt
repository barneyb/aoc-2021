package aoc2020.day01_report_repair

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    1721
    979
    366
    299
    675
    1456
""".trimIndent()

internal class ReportRepairKtTest {

    @Test
    fun partOne() {
        assertEquals(514579, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(241861950, partTwo(WORKED_EXAMPLE))
    }

}
