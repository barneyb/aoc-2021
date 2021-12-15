package util

fun <T : Comparable<T>> Collection<T>.extent(): ClosedRange<T> {
    val iterator = iterator()
    if (!iterator.hasNext()) throw IllegalArgumentException("Can't construct extent for empty collection")
    var min = iterator.next()
    var max = min
    while (iterator.hasNext()) {
        val e = iterator.next()
        if (min > e) min = e
        if (max < e) max = e
    }
    return min..max
}
