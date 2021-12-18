package day16_packet_decoder

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

class CountingIterator<T>(val delegate: Iterator<T>) : Iterator<T> {
    var count: Int = 0

    override fun hasNext() =
        delegate.hasNext()

    override fun next(): T {
        count += 1
        return delegate.next()
    }
}

fun <T> Sequence<T>.countingIterator() =
    CountingIterator(iterator())

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
