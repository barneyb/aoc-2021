package day24_arithmetic_logic_unit

/**
 * todo: add notes
 */
fun main() {
    util.solve(51983999947999, ::partOne) // 51993999947999 is too high
    util.solve(11211791111365, ::partTwo)
}

/*
inp w
mul x    0   0   0   0   0   0   0   0   0   0   0   0   0   0
add x    z   z   z   z   z   z   z   z   z   z   z   z   z   z
mod x   26  26  26  26  26  26  26  26  26  26  26  26  26  26
div z    1   1   1  26   1  26  26   1  26   1   1  26  26  26
add x   11  11  15 -14  10   0  -6  13  -3  13  15  -2  -9  -2
eql x    w   w   w   w   w   w   w   w   w   w   w   w   w   w
eql x    0   0   0   0   0   0   0   0   0   0   0   0   0   0
mul y    0   0   0   0   0   0   0   0   0   0   0   0   0   0
add y   25  25  25  25  25  25  25  25  25  25  25  25  25  25
mul y    x   x   x   x   x   x   x   x   x   x   x   x   x   x
add y    1   1   1   1   1   1   1   1   1   1   1   1   1   1
mul z    y   y   y   y   y   y   y   y   y   y   y   y   y   y
mul y    0   0   0   0   0   0   0   0   0   0   0   0   0   0
add y    w   w   w   w   w   w   w   w   w   w   w   w   w   w
add y    6  14  13   1   6  13   6   3   8  14   4   7  15   1
mul y    x   x   x   x   x   x   x   x   x   x   x   x   x   x
add z    y   y   y   y   y   y   y   y   y   y   y   y   y   y
 */

fun doRound(z: Long, r: Int, w: Long) =
    doRound(z, A[r], B[r], C[r], w)

fun doRound(z: Long, a: Long, b: Long, c: Long, w: Long) =
    if (z % 26 + b == w)
        z / a
    else
        z / a * 26 + w + c

val RE_TOKENIZE = " +".toRegex()
val A = "1   1   1  26   1  26  26   1  26   1   1  26  26  26"
    .split(RE_TOKENIZE)
    .map(String::toLong)
    .toLongArray()
val B = "11  11  15 -14  10   0  -6  13  -3  13  15  -2  -9  -2"
    .split(RE_TOKENIZE)
    .map(String::toLong)
    .toLongArray()
val C = "6  14  13   1   6  13   6   3   8  14   4   7  15   1"
    .split(RE_TOKENIZE)
    .map(String::toLong)
    .toLongArray()

fun searchIn(r: Int, targets: Set<Long>): Set<Long> {
    val next = mutableSetOf<Long>()
    for (z in -10_000..10_000L)
        findWs(z, r, targets, next)
    inputZByRound[r] = next
    return next
}

private val inputZByRound = mutableMapOf<Int, Set<Long>>()

private fun findWs(
    z: Long,
    r: Int,
    targets: Set<Long>,
    next: MutableSet<Long>
) {
    for (w in 9 downTo 1L) {
        val v = doRound(z, r, w)
        if (targets.contains(v)) {
            next.add(z)
//            println("r=$r, z=$z, w=$w yields $v")
        }
    }
}

private fun buildInputZByRound() {
    var targets = setOf(0L)
    for (r in 13 downTo 0) {
        if (targets.isEmpty()) {
            println("no targets for round $r")
            break
        }
        targets = searchIn(r, targets)
    }
}

fun partOne(input: String) =
    solve(input, 9 downTo 1L)

fun solve(input: String, digitPriority: Iterable<Long>): Long {
    // first back-track and find all the per-round output Z that might work
    buildInputZByRound()

    // now go forward and find the largest per-round W that passes the check
    fun walk(r: Int, z: Long, digits: List<Long>): Long {
        if (digits.size == 14) {
            return digits.fold(0L) { agg, n ->
                agg * 10 + n
            }
        }
        val targets = if (r == 13) setOf(0L)
        else inputZByRound[r + 1]!!
        for (w in digitPriority) {
            val v = doRound(z, r, w)
            if (targets.contains(v)) {
                val answer = walk(r + 1, v, digits + w)
                if (answer > 0) {
                    return answer
                }
            }
        }
        return -1
    }
    return walk(0, 0, emptyList())
}

fun partTwo(input: String) =
    solve(input, 1..9L)
