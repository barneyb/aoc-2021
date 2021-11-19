package aoc2020.day01_report_repair

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ReportRepairKtTest {

    @Test
    fun partOne() {
        assertEquals(
            514579, partOne(
                """
            1721
            979
            366
            299
            675
            1456
        """.trimIndent()
            )
        )
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo("input"))
    }

}
