package aoc2019.day22_slam_shuffle

fun main() {
    util.solve(3036, ::partOne)
    util.solve(2019, ::partOneReverse)
    util.solve(44714869199563, ::partTwo) // with the partial cutoff
}

internal interface Op {
    fun trace(card: Long): Long
    fun untrace(card: Long): Long
}

internal data class Cut(val deckSize: Long, val n: Long) : Op {

    override fun trace(card: Long) = (card + deckSize - n) % deckSize

    override fun untrace(card: Long) = (card + deckSize + n) % deckSize

}

internal data class Deal(val deckSize: Long, val n: Long) : Op {

    private val inverse: Long by lazy { multInverseModN(n, deckSize) }

    override fun trace(card: Long) = card * n % deckSize

    override fun untrace(card: Long) = card * inverse % deckSize

}

private fun multInverseModN(a: Long, n: Long): Long {
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

internal class Reverse(val deckSize: Long) : Op {

    override fun trace(card: Long) = deckSize - card - 1

    override fun untrace(card: Long) = deckSize - card - 1

}

internal fun String.words() = this.split(" ")

internal fun List<Op>.trace(card: Long) =
    this.fold(card) { c, op ->
        op.trace(c)
    }

internal fun List<Op>.untrace(card: Long) =
    this.asReversed().fold(card) { c, op ->
        op.untrace(c)
    }

internal fun String.toOp(deckSize: Long): Op {
    val s = this.trim()
    return when {
        s.startsWith("cut") ->
            Cut(deckSize, s.words().last().toLong())
        s.startsWith("deal with increment") ->
            Deal(deckSize, s.words().last().toLong())
        s == "deal into new stack" ->
            Reverse(deckSize)
        else ->
            throw UnsupportedOperationException("Can't parse '$this' to an Op")
    }
}

internal fun String.toOps(deckSize: Long): List<Op> =
    this.lines()
        .filter(String::isNotBlank)
        .map { it.toOp(deckSize) }

fun partOne(input: String) = input
    .toOps(10007)
    .trace(2019)

fun partOneReverse(input: String) = input
    .toOps(10007)
    .untrace(3036)

fun partTwo(input: String): Long {
    val ops = input
        .toOps(119315717514047) // 119315717514047
    var card = 2020L
    for (i in 0 until (101741582076661 / 1000 / 60 / 60 / 24)) {
        card = ops.untrace(card)
    }
    return card
}
