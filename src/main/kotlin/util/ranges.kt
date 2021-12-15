package util

val ClosedRange<Long>.length get() = endInclusive - start

val IntRange.length get() = endInclusive - start

val LongRange.length get() = endInclusive - start
