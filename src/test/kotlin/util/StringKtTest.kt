package util

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class StringKtTest {

    @Test
    fun hasCharWithMinOccurrences() {
        // zero
        assertThrows<IllegalArgumentException> {
            "".hasCharWithMinOccurrences(0)
        }

        // one
        assertFalse("".hasCharWithMinOccurrences(1))
        assertTrue("a".hasCharWithMinOccurrences(1))
        assertTrue("ab".hasCharWithMinOccurrences(1))
        assertTrue("abab".hasCharWithMinOccurrences(1))

        // two
        assertFalse("ab".hasCharWithMinOccurrences(2))
        assertTrue("aab".hasCharWithMinOccurrences(2))
        assertTrue("aba".hasCharWithMinOccurrences(2))
        assertTrue("abab".hasCharWithMinOccurrences(2))
        assertTrue("ababa".hasCharWithMinOccurrences(2))

        // many
        assertFalse("abab".hasCharWithMinOccurrences(3))
        assertTrue("ababa".hasCharWithMinOccurrences(3))
        assertTrue("baaab".hasCharWithMinOccurrences(3))
        assertTrue("baaabaaa".hasCharWithMinOccurrences(3))

        // more than length
        assertFalse("abcde".hasCharWithMinOccurrences(18))
    }

    @Test
    fun hasCharWithExactOccurrences() {
        // zero
        assertThrows<IllegalArgumentException> {
            "".hasCharWithExactOccurrences(0)
        }

        // one
        assertFalse("".hasCharWithExactOccurrences(1))
        assertTrue("a".hasCharWithExactOccurrences(1))
        assertTrue("ab".hasCharWithExactOccurrences(1))
        assertFalse("abab".hasCharWithExactOccurrences(1))

        // two
        assertFalse("ab".hasCharWithExactOccurrences(2))
        assertTrue("aab".hasCharWithExactOccurrences(2))
        assertTrue("aba".hasCharWithExactOccurrences(2))
        assertTrue("abab".hasCharWithExactOccurrences(2))
        assertFalse("ababab".hasCharWithExactOccurrences(2))

        // many
        assertFalse("abab".hasCharWithExactOccurrences(3))
        assertTrue("ababa".hasCharWithExactOccurrences(3))
        assertTrue("baaab".hasCharWithExactOccurrences(3))
        assertFalse("baaabaa".hasCharWithExactOccurrences(3))

        // more than length
        assertFalse("abcde".hasCharWithExactOccurrences(18))
    }

}
