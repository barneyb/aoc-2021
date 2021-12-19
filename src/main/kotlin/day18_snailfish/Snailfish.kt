package day18_snailfish

import util.countingIterator

/**
 * todo: add notes
 */
fun main() {
    util.solve(::partOne)
    util.solve(::partTwo)
}

private const val T_OPEN = -100
private const val T_DELIM = -1
private const val T_CLOSE = -101

data class SnailNum(var tokens: MutableList<Int>) {
    var mutationCount: Int = 0
    val size get() = tokens.size

    fun get(index: Int) = tokens[index]

    fun explode() {
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
        replace(idxToExplode - 1, idxToExplode + 4, listOf(0))
        for (i in idxToExplode - 2 downTo 0) {
            if (tokens[i] >= 0) {
                tokens[i] += left
                mutationCount += 1
                break
            }
        }
    }

    private fun replace(start: Int, end: Int, stuff: List<Int>) {
        val next = ArrayList<Int>(size - (end - start) + stuff.size)
        next.addAll(tokens.subList(0, start))
        next.addAll(stuff)
        if (end < size - 1)
            next.addAll(tokens.subList(end, size))
        tokens = next
        mutationCount += 1
    }

    fun split() {
        for (i in 0 until size) {
            if (tokens[i] >= 10) {
                val a = tokens[i] / 2
                val b = tokens[i] - a
                replace(i, i + 1, listOf(T_OPEN, a, T_DELIM, b, T_CLOSE))
                break
            }
        }
    }

    fun reduce() {
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
        val tokens = ArrayList<Int>(3 + size + other.size)
        tokens.add(T_OPEN)
        tokens.addAll(this.tokens)
        tokens.add(T_DELIM)
        tokens.addAll(other.tokens)
        tokens.add(T_CLOSE)
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
    return SnailNum(tokens)
}

fun String.snailExplode(): String {
    val num = toSnailNum()
    num.explode()
    return num.toString()
}

fun String.snailSplit(): String {
    val num = toSnailNum()
    num.split()
    return num.toString()
}

fun String.snailReduce(): String {
    val num = toSnailNum()
    num.reduce()
    return num.toString()
}

fun String.snailPlus(other: String) =
    toSnailNum().plus(other.toSnailNum()).toString()

val SnailNum.snailMagnitude: Int
    get() {
        TODO("Not yet implemented")
    }

/*
reducing: [[[[[6,6],[ 6,6]],[[ 6,0],[6,7]]],[[[ 7,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[  0  ,[12,6]],[[ 6,0],[6,7]]],[[[ 7,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,   0  ],[[12,0],[6,7]]],[[[ 7,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   0  ,[6,7]]],[[[ 7,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   6  ,  0  ]],[[[14,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   6  , 14  ]],[[   0  ,[15,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   6  , 14  ]],[[  15  ,   0  ],[17,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   6  , 14  ]],[[  15  ,   0  ],[115,0]]],[3,9]]
   split: [[[[[6,6],12],[6,14]],[[15,0],[115,0]]],[3,9]]
 explode: [[[[0,18],[6,14]],[[15,0],[115,0]]],[3,9]]
   split: [[[[0,[9,9]],[6,14]],[[15,0],[115,0]]],[3,9]]
 */

fun partOne(input: String) = input.trim().length

fun partTwo(input: String) = input.trim().length
