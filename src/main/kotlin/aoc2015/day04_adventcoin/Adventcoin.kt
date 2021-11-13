package aoc2015.day04_adventcoin

import java.security.MessageDigest

fun main() {
    util.solve(346386, ::partOne)
    // correct, but wastefully slow, so disabled
//    util.solve(9958218, ::partTwo)
}

fun ByteArray.toHex() = joinToString(separator = "") { byte ->
    "%02x".format(byte)
}

fun MessageDigest.updateForHex(it: Any): String {
    val digest = clone() as MessageDigest
    digest.update(it.toString().toByteArray())
    return digest.digest().toHex()
}

fun partOne(input: String) =
    firstCoinWithPrefix(input, "00000")

private fun firstCoinWithPrefix(input: String, prefix: String): Int {
    val inputDigest = MessageDigest.getInstance("MD5")
    inputDigest.update(input.toByteArray())
    generateSequence(0, Int::inc).forEach {
        val str = inputDigest.updateForHex(it)
        if (str.startsWith(prefix)) {
            return it
        }
    }
    throw IllegalStateException("what?!")
}

fun partTwo(input: String) =
    firstCoinWithPrefix(input, "000000")

