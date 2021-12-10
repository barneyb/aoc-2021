package day08_seven_segment

import java.util.*

fun main() {
    util.solve(390, ::partOne)
    util.solve(1011785, ::partTwo)
}

fun partOne(input: String) =
    input
        .lineSequence()
        .map { it.substring(61) }
        .flatMap { it.split(' ') }
        .map(String::length)
        .count { it == 2 || it == 3 || it == 4 || it == 7 }

typealias Segments = SortedSet<Char>

private fun String.toSegments(): List<Segments> =
    trim()
        .split(' ')
        .map(CharSequence::toSortedSet)

fun partTwo(input: String) =
    input
        .lineSequence()
        .map { it.split('|').map(String::toSegments) }
        .map { decipherOutput(it.first(), it.last()) }
        .sum()

fun decipherOutput(
    observations: List<Segments>,
    output: List<Segments>
): Int {
    val segToValue = mutableMapOf<Segments, Int>()
    val valueToSeg = mutableMapOf<Int, Segments>()
    val byLen = observations.groupBy(Segments::size)

    fun gotIt(seg: Segments, value: Int) {
        segToValue[seg] = value
        valueToSeg[value] = seg
        (byLen[seg.size] as MutableList).remove(seg)
    }

    // simple ones
    val one = byLen[2]!!.first()
    val four = byLen[4]!!.first()
    gotIt(one, 1)
    gotIt(byLen[3]!!.first(), 7)
    gotIt(four, 4)
    gotIt(byLen[7]!!.first(), 8)

    // five-segment ones
    val fourSubOne = four - one
    gotIt(byLen[5]!!.find { it.containsAll(fourSubOne) }!!, 5)
    gotIt(byLen[5]!!.find { it.containsAll(one) }!!, 3)
    gotIt(byLen[5]!!.first(), 2)

    // six segment ones
    gotIt(byLen[6]!!.find { it.containsAll(four) }!!, 9)
    gotIt(byLen[6]!!.find { it.containsAll(one) }!!, 0)
    gotIt(byLen[6]!!.first(), 6)

    return output.fold(0) { n, seg ->
        n * 10 + segToValue[seg]!!
    }
}


