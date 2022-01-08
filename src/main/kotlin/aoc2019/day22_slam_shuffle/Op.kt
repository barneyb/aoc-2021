package aoc2019.day22_slam_shuffle

import util.modularInverse as modInv
import util.modularMultiply as modMult

internal interface Op {
    fun forward(card: Long): Long
    fun reverse(card: Long): Long
}

internal data class Cut(val deckSize: Long, val n: Long) : Op {

    override fun forward(card: Long) =
        (card + deckSize - n) % deckSize

    override fun reverse(card: Long) =
        (card + deckSize + n) % deckSize

}

internal data class Deal(val deckSize: Long, val n: Long) : Op {

    private val inverse: Long by lazy { modInv(n, deckSize) }

    override fun forward(card: Long) =
        modMult(card, n, deckSize)

    override fun reverse(card: Long) =
        modMult(card, inverse, deckSize)

}

internal data class NewStack(val deckSize: Long) : Op {

    override fun forward(card: Long) =
        deckSize - card - 1

    override fun reverse(card: Long) =
        deckSize - card - 1

}

internal fun String.toOp(deckSize: Long): Op {
    val s = this.trim()
    return when {
        s.startsWith("cut") ->
            Cut(deckSize, s.words().last().toLong())
        s.startsWith("deal with increment") ->
            Deal(deckSize, s.words().last().toLong())
        s == "deal into new stack" ->
            NewStack(deckSize)
        else ->
            throw UnsupportedOperationException("Can't parse '$this' to an Op")
    }
}

internal fun String.toOps(deckSize: Long) =
    lines()
        .filter(String::isNotBlank)
        .map { it.toOp(deckSize) }

internal fun reduceOps(ops: List<Op>): List<Op> {
    @Suppress("NAME_SHADOWING")
    var ops = ops
    var i = 0
    while (i < ops.size) {
        val op = ops[i]
        if (op is NewStack) {
            ops = ops.subList(0, i) +
                    listOf(
                        Deal(op.deckSize, op.deckSize - 1),
                        Cut(op.deckSize, 1),
                    ) +
                    ops.subList(i + 1, ops.size)
            continue // redo the newly-expanded index
        }
        if (i == 0) {
            i += 1
            continue // the rest require a prev op to work with
        }
        val prev = ops[i - 1]
        if (prev is Cut && op is Cut) {
            ops = ops.subList(0, i - 1) +
                    Cut(op.deckSize, (prev.n + op.n) % op.deckSize) +
                    ops.subList(i + 1, ops.size)
            i -= 1
        } else if (prev is Deal && op is Deal) {
            ops = ops.subList(0, i - 1) +
                    Deal(op.deckSize, modMult(prev.n, op.n, op.deckSize)) +
                    ops.subList(i + 1, ops.size)
            i -= 1
        } else if (prev is Cut && op is Deal) {
            // move the cut to the other side
            ops = ops.subList(0, i - 1) +
                    listOf(
                        op,
                        Cut(op.deckSize, modMult(prev.n, op.n, op.deckSize)),
                    ) +
                    ops.subList(i + 1, ops.size)
            i -= 1
        } else {
            i += 1
        }
    }
    return ops
}
