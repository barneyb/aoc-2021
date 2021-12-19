package day18_snailfish

import util.countingIterator
import java.util.*

/**
 * The main task here is similar to rotations for balancing a binary tree. In
 * AoC style, they are nonsensical (they destroy data!), but the general idea
 * is the same: take a binary tree and mutate its structure to constrain its
 * overall height. Magnitude is another "evaluate the expression tree" like both
 * Syntax Scoring (#10) and Packet Decoder (#16).
 *
 * Part two is a simple Cartesian product search... assuming part one gave you
 * useful - and performant - primitives to work with. The non-commutative nature
 * of snailfish addition is called out as a hint, though shouldn't be news given
 * the algorithm is data-destroying.
 */
fun main() {
    util.solve(3734, ::partOne)
    // initial: 3.4ms
    // array: 1.3ms
    util.solve(4837, ::partTwo)
    // initial: 53ms
    // array: 20ms
}

private const val T_OPEN = -100
private const val T_DELIM = -1
private const val T_CLOSE = -101

class SnailNum(private var tokens: IntArray) {
    private var mutationCount: Int = 0
    private val size get() = tokens.size

    val magnitude: Int
        get() {
            val stack: Deque<Int> = ArrayDeque()
            for (t in tokens) {
                when (t) {
                    T_OPEN -> {}
                    T_DELIM -> {}
                    T_CLOSE -> {
                        val r = stack.pop() * 2
                        val l = stack.pop() * 3
                        stack.push(l + r)
                    }
                    else -> stack.push(t)
                }
            }
            if (stack.size != 1) {
                throw AssertionError("Stack didn't unwind to single value: $stack")
            }
            return stack.pop()
        }

    internal fun explode() {
        var depth = 0
        var idxToExplode = -1
        for (i in tokens.indices) {
            val c = tokens[i]
            if (c == T_OPEN) depth++
            else if (c == T_CLOSE) --depth
            else if (depth > 4) {
                idxToExplode = i
                break
            }
        }
        if (idxToExplode < 0) return // nothing to do

        val left = tokens[idxToExplode]
        val right = tokens[idxToExplode + 2]
        // do these right-to-left so indexes don't change
        for (i in idxToExplode + 3 until size) {
            if (tokens[i] >= 0) {
                tokens[i] += right
                mutationCount += 1
                break
            }
        }
        replace(idxToExplode - 1, idxToExplode + 4, intArrayOf(0))
        for (i in idxToExplode - 2 downTo 0) {
            if (tokens[i] >= 0) {
                tokens[i] += left
                mutationCount += 1
                break
            }
        }
    }

    private fun replace(start: Int, end: Int, stuff: IntArray) {
        val next = IntArray(size - (end - start) + stuff.size)
        tokens.copyInto(next, 0, 0, start)
        stuff.copyInto(next, start)
        if (end < size - 1)
            tokens.copyInto(next, start + stuff.size, end)
        tokens = next
        mutationCount += 1
    }

    internal fun split() {
        for (i in 0 until size) {
            if (tokens[i] >= 10) {
                val a = tokens[i] / 2
                val b = tokens[i] - a
                replace(i, i + 1, intArrayOf(T_OPEN, a, T_DELIM, b, T_CLOSE))
                break
            }
        }
    }

    internal fun reduce() {
        while (true) {
            val mc = mutationCount
            explode()
            if (mc < mutationCount) {
                continue
            }
            split()
            if (mc < mutationCount) {
                continue
            }
            break
        }
    }

    operator fun plus(other: SnailNum): SnailNum {
        val tokens = IntArray(3 + size + other.size)
        tokens[0] = T_OPEN
        this.tokens.copyInto(tokens, 1)
        tokens[size + 1] = T_DELIM
        other.tokens.copyInto(tokens, size + 2)
        tokens[tokens.size - 1] = T_CLOSE
        val num = SnailNum(tokens)
        num.reduce()
        return num
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (t in tokens) {
            when {
                t == T_OPEN -> sb.append('[')
                t == T_DELIM -> sb.append(',')
                t == T_CLOSE -> sb.append(']')
                else -> sb.append(t)
            }
        }
        return sb.toString()
    }

}

fun String.toSnailNum(): SnailNum {
    val tokens = mutableListOf<Int>()
    val itr = iterator().countingIterator()
    for (c in itr) {
        when (c) {
            in '0'..'9' -> {
                val n = c.digitToInt()
                if (tokens.isEmpty() || tokens.last() < 0)
                    tokens.add(n)
                else
                    tokens[tokens.size - 1] = tokens.last() * 10 + n
            }
            '[' -> tokens.add(T_OPEN)
            ',' -> tokens.add(T_DELIM)
            ']' -> tokens.add(T_CLOSE)
            ' ', '\t', '\n' -> {}
            else -> throw IllegalArgumentException("Unknown '$c' at ${itr.count}")
        }
    }
    return SnailNum(tokens.toIntArray())
}

fun partOne(input: String) =
    input
        .lines()
        .map(String::toSnailNum)
        .reduce(SnailNum::plus)
        .magnitude

fun partTwo(input: String) =
    input
        .lines()
        .map(String::toSnailNum)
        .let { numbers ->
            numbers.maxOf { a ->
                numbers.maxOf { b ->
                    if (a == b) -1
                    else (a + b).magnitude
                }
            }
        }
