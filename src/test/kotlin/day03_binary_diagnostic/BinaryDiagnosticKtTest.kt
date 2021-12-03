package day03_binary_diagnostic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    00100
    11110
    10110
    10111
    10101
    01111
    00111
    11100
    10000
    11001
    00010
    01010
""".trimIndent()

internal class BinaryDiagnosticKtTest {

    @Test
    fun partOne() {
        assertEquals(198, partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
