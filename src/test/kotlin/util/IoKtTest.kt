package util

import org.junit.jupiter.api.Test

internal class IoKtTest {

    @Test
    fun doesAnswerSmoke() {
        answer(1234)
        answer(1234, 53)
        answer("SKEOSF", 1234)
        answer("SKEOSF", 123456)
        answer(45648988944, 789)
    }

}
