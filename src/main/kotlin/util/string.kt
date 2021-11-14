package util

fun String.hasCharWithMinOccurrences(n: Int): Boolean {
    when {
        // makes no sense
        n < 1 -> throw IllegalArgumentException()
        // easy!
        n == 1 -> return length > 0
        // impossible!
        n > length -> return false
        // a set is sufficient
        n == 2 -> {
            val seen = hashSetOf<Char>()
            for (i in 0 until length) {
                if (!seen.add(this[i])) {
                    return true
                }
            }
        }
        // need an actual histogram
        else -> {
            val hist = hashMapOf<Char, Int>()
            for (i in 0 until length) {
                if (hist.merge(this[i], 1, Int::plus) == n) {
                    return true
                }
            }
        }
    }
    return false
}

fun String.hasCharWithExactOccurrences(n: Int) = when {
    // makes no sense
    n < 1 -> throw IllegalArgumentException()
    // impossible!
    n > length -> false
    // build a histogram and inspect
    else -> {
        val hist = hashMapOf<Int, Int>()
        chars().forEach {
            hist.merge(it, 1, Int::plus)
        }
        hist.any { (_, count) -> count == n }
    }
}
