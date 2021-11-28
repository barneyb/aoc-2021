package util

import java.util.*

fun <E, K : Comparable<K>> discreteHistogram(
    items: Iterable<E>,
    keySelector: (E) -> K,
): Map<K, Int> = items.groupingBy(keySelector).eachCount()

private val LongRange.length
    get() = last - first

private fun LongRange.expandTo(n: Long) =
    if (n < first)
        n..last
    else if (n > last)
        first..n
    else
        this

fun <E> continuousHistogram(
    items: Iterable<E>,
    bucketCount: Int = 15,
    keySelector: (E) -> Long,
): Map<Long, Int> {
    val pairs = items.map {
        Pair(keySelector(it), it)
    }
    val first = pairs.first().first
    val range = pairs
        .fold(first..first) { r, p ->
            r.expandTo(p.first)
        }
    val width = range.length / bucketCount + bucketCount // correct truncation
    val hist = pairs
        .asSequence()
        .map { p ->
            (p.first - range.first) / width * width + range.first
        }
        .fold(TreeMap<Long, Int>()) { map, b ->
            map.merge(b, 1, Int::plus)
            map
        }
    for (b in range step width) {
        hist.putIfAbsent(b, 0)
    }
    return hist
}

fun <E> Map<E, Int>.makeComplete(
    items: Iterable<E>,
) = toMutableMap()
    .let { result ->
        items.forEach {
            result.putIfAbsent(it, 0)
        }
        result
    }
