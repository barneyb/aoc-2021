package day08_seven_segment

import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor

fun main() {
    util.solve(390, ::partOne)
    util.solve(1011785, ::partTwo)
    // ~1.24ms w/ SortedSet<Char>
    // ~800μs w/ Int
    // ~750μs w/ Byte
}

fun partOne(input: String) =
    input
        .lineSequence()
        .map { it.substring(61) }
        .flatMap { it.split(' ') }
        .map(String::length)
        .count { it == 2 || it == 3 || it == 4 || it == 7 }

typealias Segments = Byte

private fun String.toSegments(): Segments =
    fold(0) { agg, c ->
        when (c) {
            'a' -> agg or 0b1
            'b' -> agg or 0b10
            'c' -> agg or 0b100
            'd' -> agg or 0b1000
            'e' -> agg or 0b10000
            'f' -> agg or 0b100000
            'g' -> agg or 0b1000000
            else -> throw IllegalArgumentException("unknown segment '$c'")
        }
    }

fun partTwo(input: String) =
    input
        .lineSequence()
        .map { line ->
            line.split('|')
                .map {
                    it.trim()
                        .split(' ')
                        .map(String::toSegments)
                }
        }
        .map { decipherOutput(it.first(), it.last()) }
        .sum()

fun decipherOutput(
    observations: List<Segments>,
    output: List<Segments>
): Int {
    val segToValue = mutableMapOf<Segments, Int>()
    val valueToSeg = mutableMapOf<Int, Segments>()
    val byLen = observations.groupBy(Segments::countOneBits)

    fun gotIt(seg: Segments, value: Int) {
        segToValue[seg] = value
        valueToSeg[value] = seg
        (byLen[seg.countOneBits()] as MutableList).remove(seg)
    }

    // simple ones
    val one = byLen[2]!!.first()
    val four = byLen[4]!!.first()
    gotIt(one, 1)
    gotIt(byLen[3]!!.first(), 7)
    gotIt(four, 4)
    gotIt(byLen[7]!!.first(), 8)

    // five-segment ones
    val fourSubOne = four xor one
    gotIt(byLen[5]!!.find { it and fourSubOne == fourSubOne }!!, 5)
    gotIt(byLen[5]!!.find { it and one == one }!!, 3)
    gotIt(byLen[5]!!.first(), 2)

    // six segment ones
    gotIt(byLen[6]!!.find { it and four == four }!!, 9)
    gotIt(byLen[6]!!.find { it and one == one }!!, 0)
    gotIt(byLen[6]!!.first(), 6)

    return output.fold(0) { n, seg ->
        n * 10 + segToValue[seg]!!
    }
}


