package histogram

typealias Histogram<E> = Map<E, Long>

typealias MutableHistogram<E> = MutableMap<E, Long>

/**
 * Adds `times` (one, by default) instances of `item` to the histogram, and
 * returns `item`'s new total count.
 */
fun <E> MutableHistogram<E>.count(item: E, times: Long = 1) =
    merge(item, times, Long::plus)!!

/**
 * Include `item` in the histogram's domain, with zero instances.
 */
fun <E> MutableHistogram<E>.unobserved(item: E) =
    putIfAbsent(item, 0)

fun <E> histogramOf(vararg items: E): Histogram<E> =
    mutableHistogramOf(*items)

fun <E> histogramOf(items: Iterable<E>): Histogram<E> =
    mutableHistogramOf(items)

fun <E> histogramOf(items: Sequence<E>): Histogram<E> =
    mutableHistogramOf(items)

fun <E> mutableHistogramOf(): MutableHistogram<E> = HashMap()

fun <E> mutableHistogramOf(capacity: Int): MutableHistogram<E> =
    HashMap(capacity)

fun <E> mutableHistogramOf(vararg items: E) =
    mutableHistogramOf(items.asSequence())

fun <E> mutableHistogramOf(items: Iterable<E>) =
    mutableHistogramOf(items.asSequence())

fun <E> mutableHistogramOf(items: Sequence<E>) =
    mutableHistogramOf<E>()
        .also { items.forEach(it::count) }

fun <E> buildHistogram(action: MutableHistogram<E>.() -> Unit): Histogram<E> =
    mutableHistogramOf<E>().apply(action)

fun <E> buildHistogram(
    capacity: Int,
    action: MutableHistogram<E>.() -> Unit
): Histogram<E> =
    mutableHistogramOf<E>(capacity).apply(action)

val Histogram<*>.total get() = values.sum()

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
    val hist = mutableHistogramOf(pairs
        .asSequence()
        .map { p ->
            (p.first - range.first) / width * width + range.first
        })
    (range step width).forEach(hist::unobserved)
    return hist.toSortedMap()
}

/**
 * I return a new histogram with every element of `items` included with a count
 * of zero, if not already present.
 */
fun <E> Histogram<E>.makeComplete(items: Iterable<E>): Histogram<E> =
    toMutableMap()
        .also { items.forEach(it::unobserved) }
