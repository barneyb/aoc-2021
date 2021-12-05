package histogram

typealias Histogram<E> = Map<E, Int>

typealias MutableHistogram<E> = MutableMap<E, Int>

/**
 * Adds `times` (one, by default) instances of `item` to the histogram, and
 * returns `items` new total count.
 */
fun <E> MutableHistogram<E>.count(item: E, times: Int = 1) =
    merge(item, times, Int::plus)!!

/**
 * Include `item` in the histogram's domain, with zero instances.
 */
fun <E> MutableHistogram<E>.unobserved(item: E) =
    putIfAbsent(item, 0)

fun <E> histogramOf(items: Iterable<E>) =
    histogramOf(items.asSequence())

fun <E> histogramOf(items: Sequence<E>) =
    HashMap<E, Int>()
        .also { items.forEach(it::count) }

fun <E, K : Comparable<K>> histogramOf(
    items: Iterable<E>,
    keySelector: (E) -> K,
): Histogram<K> = histogramOf(items.asSequence(), keySelector)

fun <E, K : Comparable<K>> histogramOf(
    items: Sequence<E>,
    keySelector: (E) -> K,
): Histogram<K> = items.groupingBy(keySelector).eachCount()

private val LongRange.length
    get() = last - first + 1

private fun LongRange.expandTo(n: Long) =
    if (n < first)
        n..last
    else if (n > last)
        first..n
    else
        this

fun <E> continuousHistogramOf(
    items: Iterable<E>,
    bucketCount: Int = 15,
    keySelector: (E) -> Long,
): Histogram<Long> {
    val pairs = items.map {
        Pair(keySelector(it), it)
    }
    val first = pairs.first().first
    val range = pairs
        .fold(first..first) { r, p ->
            r.expandTo(p.first)
        }
    val width = range.length / bucketCount + bucketCount // correct truncation
    val hist = histogramOf(pairs
        .asSequence()
        .map { p ->
            (p.first - range.first) / width * width + range.first
        })
    for (b in range step width) {
        hist.putIfAbsent(b, 0)
    }
    return hist.toSortedMap()
}

/**
 * I return a new histogram with every element of `items` included with a count
 * of zero, if not already present.
 */
fun <E> Histogram<E>.makeComplete(items: Iterable<E>): Histogram<E> =
    toMutableMap()
        .also { items.forEach(it::unobserved) }
