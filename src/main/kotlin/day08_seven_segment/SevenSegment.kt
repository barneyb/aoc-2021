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

private infix fun Segments.overlaps(other: Segments) =
    this and other == other

private fun <K, E> Map<K, Collection<E>>.pull(key: K): E {
    val items = get(key)!!
    if (items.size == 1) return items.first()
    throw IllegalStateException("Multiple items exist for key")
}

private fun <K, E> Map<K, Collection<E>>.pull(
    key: K,
    predicate: (E) -> Boolean
): E {
    val items = get(key)!!
    if (items.size == 1 && predicate(items.first())) return items.first()
    val matches = items.filter(predicate)
    if (matches.size == 1) return matches.first()
    if (matches.isEmpty()) throw NoSuchElementException()
    throw IllegalStateException("Multiple items match predicate")
}

fun decipherOutput(
    observations: List<Segments>,
    output: List<Segments>
): Int {
    val segToValue = mutableMapOf<Segments, Int>()
    val valueToSeg = mutableMapOf<Int, Segments>()
    val byLen = observations.groupBy(Segments::countOneBits)

    fun Segments.means(value: Int): Segments {
        segToValue[this] = value
        valueToSeg[value] = this
        (byLen[countOneBits()] as MutableList).remove(this)
        return this
    }

    // simple ones
    val one = byLen.pull(2).means(1)
    val four = byLen.pull(4).means(4)
    byLen.pull(3).means(7)
    byLen.pull(7).means(8)

    // five-segment ones
    val fourSubOne = four xor one
    byLen.pull(5) { it overlaps fourSubOne }.means(5)
    byLen.pull(5) { it overlaps one }.means(3)
    byLen.pull(5).means(2)

    // six segment ones
    byLen.pull(6) { it overlaps four }.means(9)
    byLen.pull(6) { it overlaps one }.means(0)
    byLen.pull(6).means(6)

    return output.fold(0) { n, seg ->
        n * 10 + segToValue[seg]!!
    }
}


