package aoc2019.day04_secure_container

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SecureContainerKtTest {

    @Test
    fun partOne() {
        assertTrue(111111.isValidPassword())
        assertFalse(223450.isValidPassword())
        assertFalse(123789.isValidPassword())
    }

}
