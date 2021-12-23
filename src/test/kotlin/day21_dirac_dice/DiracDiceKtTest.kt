package day21_dirac_dice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    Player 1 starting position: 4
    Player 2 starting position: 8
""".trimIndent()

internal class DiracDiceKtTest {

    @Test
    fun partOne() {
        assertEquals(739785, partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
