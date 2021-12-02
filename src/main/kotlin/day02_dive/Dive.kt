package day02_dive

fun main() {
    util.solve(1840243, ::partOne)
    util.solve(1727785422, ::partTwo)
}

interface Sub<T : Sub<T>> {
    val pos: Int
    val depth: Int
    fun forward(n: Int): T
    fun up(n: Int): T
    fun down(n: Int): T

    val finalDestination
        get() = pos * depth
}

data class Sub1(
    override val pos: Int = 0,
    override val depth: Int = 0
) : Sub<Sub1> {

    override fun forward(n: Int) =
        copy(pos = pos + n)

    override fun up(n: Int) =
        copy(depth = depth - n)

    override fun down(n: Int) =
        copy(depth = depth + n)

}

private fun <T : Sub<T>> drive(sub: Sub<T>, line: String): Sub<T> {
    val words = line.split(" ")
    val n = words[1].toInt()
    return when (words[0]) {
        "forward" -> sub.forward(n)
        "up" -> sub.up(n)
        "down" -> sub.down(n)
        else -> throw IllegalArgumentException("Unrecognized '$line' instruction")
    }
}

fun partOne(input: String) =
    input
        .lineSequence()
        .fold(Sub1(), ::drive)
        .finalDestination

data class Sub2(
    override val pos: Int = 0,
    override val depth: Int = 0,
    val aim: Int = 0
) : Sub<Sub2> {

    override fun forward(n: Int) =
        copy(pos = pos + n, depth = depth + aim * n)

    override fun up(n: Int) =
        copy(aim = aim - n)

    override fun down(n: Int) =
        copy(aim = aim + n)

}

fun partTwo(input: String) =
    input
        .lineSequence()
        .fold(Sub2(), ::drive)
        .finalDestination
