package aoc2015.day04_adventcoin

import util.countForever
import java.security.MessageDigest
import kotlin.experimental.and

fun main() {
    util.solve(346386, ::partOne)
    util.solve(9958218, ::partTwo)
}

fun MessageDigest.updateForBytes(it: Any): ByteArray {
    val digest = clone() as MessageDigest
    digest.update(it.toString().toByteArray())
    return digest.digest()
}

fun partOne(input: String) =
    firstCoinWithZeros(input, 5)

private fun firstCoinWithZeros(input: String, zeroCount: Int): Int {
    val inputDigest = MessageDigest.getInstance("MD5")
    inputDigest.update(input.toByteArray())
    countForever().forEach {
        if (inputDigest.updateForBytes(it).startsWithHexZeros(zeroCount)) {
            return it
        }
    }
    throw IllegalStateException("what?!")
}

private const val ZERO = (0).toByte()
private const val TOP_HALF = (0xf0).toByte()

fun ByteArray.startsWithHexZeros(n: Int): Boolean {
    // each hex digit represents half a byte
    val end = n / 2
    for (i in 0 until end) {
        if (this[i] != ZERO) {
            return false
        }
    }
    if (n % 2 != 0) {
        // and half the next byte
        return this[end] and TOP_HALF == ZERO
    }
    return true
}

fun partTwo(input: String) =
    firstCoinWithZeros(input, 6)

