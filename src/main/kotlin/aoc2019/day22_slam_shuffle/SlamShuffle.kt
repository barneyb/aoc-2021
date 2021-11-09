package aoc2019.day22_slam_shuffle

fun main() {
    util.solve(3036, ::partOne)
    util.solve(2019, ::partOneReverse)
    util.solve(44714869199563, ::partTwo) // with partial cutoff
    util.solve(72115501803291, ::partTwoReverse) // with partial cutoff
}

internal interface Op {
    fun forward(card: Long): Long
    fun reverse(card: Long): Long
}

// Well, subtract. But whatever.
internal data class Add(val deckSize: Long, val n: Long) : Op {

    override fun forward(card: Long) = (card + deckSize - n) % deckSize

    override fun reverse(card: Long) = (card + deckSize + n) % deckSize

}

internal data class Multiply(val deckSize: Long, val n: Long) : Op {

    private val inverse: Long by lazy { multiplicativeInverseModN(n, deckSize) }

    override fun forward(card: Long) = card * n % deckSize

    override fun reverse(card: Long) = card * inverse % deckSize

}

private fun multiplicativeInverseModN(a: Long, n: Long): Long {
    var t = 0L
    var newt = 1L
    var r = n
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

    if (r > 1) throw IllegalArgumentException("$a is not invertible mod $n")
    if (t < 0) t += n

    return t
}

internal data class Negate(val deckSize: Long) : Op {

    override fun forward(card: Long) = deckSize - card - 1

    override fun reverse(card: Long) = deckSize - card - 1

}

internal fun String.words() = this.split(" ")

internal fun List<Op>.forward(card: Long) =
    this.fold(card) { c, op ->
        op.forward(c)
    }

internal fun List<Op>.reverse(card: Long) =
    this.asReversed().fold(card) { c, op ->
        op.reverse(c)
    }

internal fun String.toOp(deckSize: Long): Op {
    val s = this.trim()
    return when {
        s.startsWith("cut") ->
            Add(deckSize, s.words().last().toLong())
        s.startsWith("deal with increment") ->
            Multiply(deckSize, s.words().last().toLong())
        s == "deal into new stack" ->
            Negate(deckSize)
        else ->
            throw UnsupportedOperationException("Can't parse '$this' to an Op")
    }
}

internal fun String.toOps(deckSize: Long): List<Op> =
    this.lines()
        .filter(String::isNotBlank)
        .map { it.toOp(deckSize) }

const val DECK_SIZE_ONE: Long = 10007

fun partOne(input: String) =
    partOne(input, DECK_SIZE_ONE, 2019, 1)

fun partOne(
    input: String,
    deckSize: Long,
    card: Long,
    iterations: Long
): Long {
    val ops = input.toOps(deckSize)
    var c = card
    for (i in 0 until if (iterations > 100000) iterations / 1000 / 60 / 60 / 24 else iterations) {
        c = ops.forward(c)
    }
    return c
}

fun partOneReverse(input: String) = input
    .toOps(DECK_SIZE_ONE)
    .reverse(3036)

const val DECK_SIZE_TWO: Long = 119_315_717_514_047

const val ITERATIONS_TWO: Long = 101_741_582_076_661

fun partTwo(input: String) =
    partTwo(
        input,
        DECK_SIZE_TWO,
        2020,
        ITERATIONS_TWO
    )

internal fun partTwo(
    input: String,
    deckSize: Long,
    card: Long,
    iterations: Long
): Long {
    val ops = input.toOps(deckSize)
    var c = card
    for (i in 0 until if (iterations > 100000) iterations / 1000 / 60 / 60 / 24 else iterations) {
        c = ops.reverse(c)
    }
    return c
}

fun partTwoReverse(input: String) =
    partOne(
        input,
        DECK_SIZE_TWO,
        2020,
        DECK_SIZE_TWO - ITERATIONS_TWO - 1
    )
