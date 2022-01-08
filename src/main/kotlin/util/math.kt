package util

fun modularMultiply(a: Long, b: Long, mod: Long): Long {
    @Suppress("NAME_SHADOWING")
    var a = a % mod

    @Suppress("NAME_SHADOWING")
    var b = b
    var res = 0L
    while (b > 0) {
        if ((b and 1) > 0) {
            res = (res + a) % mod
        }
        a = (2 * a) % mod
        b = b shr 1
    }
    return res
}

fun modularInverse(a: Long, mod: Long): Long {
    var t = 0L
    var newt = 1L
    var r = mod
    var newr = a

    while (newr != 0L) {
        val quotient = r / newr
        val nextt = t - quotient * newt
        t = newt
        newt = nextt
        val nextr = r - quotient * newr
        r = newr
        newr = nextr
    }

    if (r > 1) throw IllegalArgumentException("$a is not invertible mod $mod")
    if (t < 0) t += mod

    return t
}
