package day24_arithmetic_logic_unit

/**
 * At first blush, this looks like a "build an interpreter/VM" sort of problem
 * in the style of Intcode. However, 9^14 numbers to check... This is another
 * "find the optimal path" (like Chiton #15), but the thing you need to find a
 * path through is expressed by the input program's _behavior_, not a map you
 * are provided. The only info you know is that the final Z must be zero, so
 * pruning the "paths" has to be done backwards. You don't necessarily need an
 * interpreter either; manually disassembling to identify the algorithm, and
 * then re-coding in your solution language will work just as well.
 *
 * Part two is just inverting the fitness function: find min vs find max.
 */
fun main() {
    util.solve(51983999947999, ::partOne) // 51993999947999 is too high
    util.solve(11211791111365, ::partTwo)
}

private typealias Round = List<Op>

private fun Round.execute(z: Long, w: Long) =
    Machine().let { m ->
        m.z = z
        m.execute(this, listOf(w))
        m.z
    }

private fun Round.searchIn(targets: Set<Long>): Set<Long> {
    val candidates = mutableSetOf<Long>().also { cand ->
        for (t in targets) {
            // each round is about a factor of 26, one way or the other
            cand.addAll((t - 1) / 26..(t + 1) / 26)
            cand.addAll((t - 1) * 26..(t + 1) * 26)
        }
    }
    return mutableSetOf<Long>().also { next ->
        for (z in candidates) {
            for (w in 1..9L)
                if (targets.contains(execute(z, w)))
                    next.add(z)
        }
    }
}

private typealias Slices = List<Round>

private fun Slices.buildInputZByRound(): Map<Int, Set<Long>> =
    mutableMapOf<Int, Set<Long>>().also { byRound ->
        ((size - 1) downTo 0).fold(setOf(0L)) { targets, r ->
            if (targets.isEmpty()) {
                throw IllegalStateException("no targets for round $r")
            }
            byRound[r] = this[r].searchIn(targets)
            byRound[r]!!
        }
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
        val targets = if (r == slices.size - 1) setOf(0L)
        else inputZByRound[r + 1]!!
        for (w in digitPriority) {
            val v = slices[r].execute(z, w)
            if (targets.contains(v)) {
                return walk(r + 1, v, digits + w)
            }
        }
        throw IllegalStateException("no round $r digit hit a target: $targets")
    }
    return walk(0, 0, emptyList())
}

fun partTwo(input: String) =
    solve(input, 1..9L)
