package day24_arithmetic_logic_unit

/**
 * todo: add notes
 */
fun main() {
    util.solve(51983999947999, ::partOne) // 51993999947999 is too high
    util.solve(11211791111365, ::partTwo)
}

typealias Program = List<Op>

fun Program.execute(z: Long, w: Long): Long {
    val m = Machine()
    m.z = z
    m.execute(this, listOf(w))
    return m.z
}

typealias Slices = List<Program>

fun Program.searchIn(targets: Set<Long>): Set<Long> {
    val next = mutableSetOf<Long>()
    for (z in -10_000..10_000L)
        findWs(z, targets, next)
    return next
}

private fun Program.findWs(
    z: Long,
    targets: Set<Long>,
    next: MutableSet<Long>
) {
    for (w in 9 downTo 1L) {
        val v = execute(z, w)
        if (targets.contains(v)) {
            next.add(z)
        }
    }
}

private fun Slices.buildInputZByRound(): Map<Int, Set<Long>> {
    val inputZByRound = mutableMapOf<Int, Set<Long>>()
    var targets = setOf(0L)
    for (r in (size - 1) downTo 0) {
        if (targets.isEmpty()) {
            println("no targets for round $r")
            break
        }
        targets = this[r].searchIn(targets)
        inputZByRound[r] = targets
    }
    return inputZByRound
}

fun partOne(input: String) =
    solve(input, 9 downTo 1L)

fun solve(input: String, digitPriority: Iterable<Long>): Long {
    // break the problem into per-digit rounds
    val m = Machine()
    val slices = m.parse(input)
        .chunked(18)

    // back-track and find all the per-round output Z that might work
    val inputZByRound = slices.buildInputZByRound()

    // go forward and find the largest per-round W that passes the check
    fun walk(r: Int, z: Long, digits: List<Long>): Long {
        if (digits.size == 14) {
            return digits.fold(0L) { agg, n ->
                agg * 10 + n
            }
        }
        val targets = if (r == 13) setOf(0L)
        else inputZByRound[r + 1]!!
        for (w in digitPriority) {
            val v = slices[r].execute(z, w)
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
