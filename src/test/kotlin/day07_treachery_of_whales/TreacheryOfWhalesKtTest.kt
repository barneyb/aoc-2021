package day07_treachery_of_whales

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    16,1,2,0,4,2,7,1,2,14
""".trimIndent()

internal class TreacheryOfWhalesKtTest {

    @Test
    fun partOne() {
        assertEquals(37, partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
