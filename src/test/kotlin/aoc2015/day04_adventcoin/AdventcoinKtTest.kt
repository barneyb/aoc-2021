package aoc2015.day04_adventcoin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.security.MessageDigest

internal class AdventcoinKtTest {

    @Test
    fun partOne() {
        assertEquals(609043, partOne("abcdef"))
        assertEquals(1048970, partOne("pqrstuv"))
    }

    @Test
    fun startsWithFiveHexZeros() {
        val md = MessageDigest.getInstance("MD5")
        md.update("iwrupvqb346386".toByteArray())
        val bytes = md.digest()
        assert(bytes.startsWithHexZeros(0))
        assert(bytes.startsWithHexZeros(1))
        assert(bytes.startsWithHexZeros(2))
        assert(bytes.startsWithHexZeros(3))
        assert(bytes.startsWithHexZeros(4))
        assert(bytes.startsWithHexZeros(5))
        assert(!bytes.startsWithHexZeros(6))
        assert(!bytes.startsWithHexZeros(7))
        assert(!bytes.startsWithHexZeros(8))
    }

    @Test
    fun startsWithSixHexZeros() {
        val md = MessageDigest.getInstance("MD5")
        md.update("iwrupvqb9958218".toByteArray())
        val bytes = md.digest()
        assert(bytes.startsWithHexZeros(0))
        assert(bytes.startsWithHexZeros(1))
        assert(bytes.startsWithHexZeros(2))
        assert(bytes.startsWithHexZeros(3))
        assert(bytes.startsWithHexZeros(4))
        assert(bytes.startsWithHexZeros(5))
        assert(bytes.startsWithHexZeros(6))
        assert(!bytes.startsWithHexZeros(7))
        assert(!bytes.startsWithHexZeros(8))
    }

}

