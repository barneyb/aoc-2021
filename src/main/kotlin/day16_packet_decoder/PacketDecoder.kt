package day16_packet_decoder

import util.CountingIterator
import util.countingIterator

/**
 * Binary things again! "One or more sub-packets" means recursion. Lots of
 * bookkeeping. But a simple question: find all the version numbers and sum.
 * Nailing the parsing is key, since the input data looks like an optimized wire
 * packet with its non-byte-aligned fields. "Make an int from these three bits."
 * Keeping track of your position in the stream is also needed, both number of
 * packets _and_ number of bits.
 *
 * It's super-simple IntCode, all in one day! Recurse through the packet,
 * interpreting the type IDs, just like any other expression evaluator. Like
 * the tiles for part two of Chiton Risk (#15), a good `Packet` interface makes
 * this a breeze.
 */
fun main() {
    util.solve(960, ::partOne)
    util.solve(12301926782560, ::partTwo) // 197445829014 is too low
}

interface Packet {
    val version: Int
    val type: Int
    val versionSum: Int

    fun evaluate(): Long
}

data class Literal(
    override val version: Int,
    override val type: Int,
    val value: Long
) : Packet {
    override val versionSum
        get() = version

    override fun evaluate() =
        value
}

data class Operator(
    override val version: Int,
    override val type: Int,
    val operands: List<Packet>
) : Packet {
    override val versionSum
        get() =
            version + operands.sumOf(Packet::versionSum)

    override fun evaluate(): Long =
        when (type) {
            0 -> operands.sumOf(Packet::evaluate)
            1 -> operands.map(Packet::evaluate).reduce(Long::times)
            2 -> operands.minOf(Packet::evaluate)
            3 -> operands.maxOf(Packet::evaluate)
            5 -> {
                val (a, b) = operands.map(Packet::evaluate)
                if (a > b) 1 else 0
            }
            6 -> {
                val (a, b) = operands.map(Packet::evaluate)
                if (a < b) 1 else 0
            }
            7 -> {
                val (a, b) = operands.map(Packet::evaluate)
                if (a == b) 1 else 0
            }
            else -> throw IllegalStateException("Unknown '$type' operator type")
        }
}

fun String.asHexBitSequence(): CountingIterator<Boolean> {
    val hexDigits = iterator()
    var buffer = 0
    var bitIdx = -1
    // sorta silly...
    return generateSequence {
        if (bitIdx < 0) {
            if (!hexDigits.hasNext()) return@generateSequence null
            bitIdx = 3
            buffer = hexDigits.next().digitToInt(16)
        }
        buffer and (1 shl bitIdx--) != 0
    }.countingIterator()
}

fun Iterator<Boolean>.readInt(bits: Int): Int =
    (0 until bits).fold(0) { incoming, _ ->
        (incoming shl 1) + if (next()) 1 else 0
    }

fun CountingIterator<Boolean>.readPacket(): Packet {
    val version = readInt(3)
    val typeId = readInt(3)
    if (typeId == 4) {
        return Literal(version, typeId, readLiteral())
    } else if (next()) {
        // packet-length operator
        val len = readInt(11)
        return Operator(
            version,
            typeId,
            (0 until len).map { readPacket() },
        )
    } else {
        // bit-length operator
        val operands = mutableListOf<Packet>()
        val len = readInt(15)
        val end = count + len
        while (count < end) operands.add(readPacket())
        return Operator(version, typeId, operands)
    }
}

private fun Iterator<Boolean>.readLiteral(): Long {
    var v = 0L
    do {
        val segment = readInt(5)
        v = (v shl 4) + (segment and 0b01111)
    } while (segment and 0b10000 > 1)
    return v
}

fun String.toPacket(): Packet =
    asHexBitSequence().readPacket()

fun partOne(input: String) =
    input.toPacket().versionSum

fun partTwo(input: String) =
    input.toPacket().evaluate()
