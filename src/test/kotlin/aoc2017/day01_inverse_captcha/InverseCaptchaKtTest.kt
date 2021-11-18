package aoc2017.day01_inverse_captcha

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class InverseCaptchaKtTest {

    @Test
    fun partOne() {
        assertEquals(3, partOne("1122"))
        assertEquals(4, partOne("1111"))
        assertEquals(0, partOne("1234"))
        assertEquals(9, partOne("91212129"))
    }

    @Test
    fun partTwo() {
        assertEquals(6, partTwo("1212"))
        assertEquals(0, partTwo("1221"))
        assertEquals(4, partTwo("123425"))
        assertEquals(12, partTwo("123123"))
        assertEquals(4, partTwo("12131415"))
    }

}
