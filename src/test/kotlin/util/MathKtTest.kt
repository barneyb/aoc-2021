package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MathKtTest {

    @Test
    fun modMult() {
        assertEquals(12, modularMultiply(3, 4, 37))
        assertEquals(5, modularMultiply(3, 4, 7))
    }

}
