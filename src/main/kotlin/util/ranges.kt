package util

val ClosedRange<Long>.length get() = endInclusive - start

val IntRange.length get() = endInclusive - start

val LongRange.length get() = endInclusive - start

fun LongRange.intersects(b: LongRange) =
    b.first in first..last || b.last in first..last

fun LongRange.contains(b: LongRange) =
    b.first in first..last && b.last in first..last

val LongRange.size
    get() =
        last - first + 1
