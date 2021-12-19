package util

class CountingIterator<T>(val delegate: Iterator<T>) : Iterator<T> {
    var count: Int = 0

    override fun hasNext() =
        delegate.hasNext()

    override fun next(): T {
        count += 1
        return delegate.next()
    }
}

fun <T> Iterator<T>.countingIterator() =
    CountingIterator(this)

fun <T> Iterable<T>.countingIterator() =
    CountingIterator(iterator())

fun <T> Sequence<T>.countingIterator() =
    CountingIterator(iterator())
