package day02_dive

fun main() {
    util.solve(1840243, ::partOne)
    util.solve(1727785422, ::partTwo)
    util.solve(1727785422, ::partTwoLoop)
    util.solve(1727785422, ::partTwoStateful)
}

interface OceanLocation {
    val pos: Int
    val depth: Int

    /**
     * The product of position and depth. No Manhattan distance this year!
     */
    val location
        get() = pos * depth
}

interface Coords<T : Coords<T>> : OceanLocation {
    fun forward(n: Int): T
    fun up(n: Int): T
    fun down(n: Int): T
}

data class Coords1(
    override val pos: Int = 0,
    override val depth: Int = 0
) : Coords<Coords1> {

    override fun forward(n: Int) =
        copy(pos = pos + n)

    override fun up(n: Int) =
        copy(depth = depth - n)

    override fun down(n: Int) =
        copy(depth = depth + n)

}

private fun <T : Coords<T>> Coords<T>.follow(input: String) =
    input
        .lineSequence()
        .fold(this) { c, line ->
            val words = line.split(" ")
            val n = words[1].toInt()
            when (words[0]) {
                "forward" -> c.forward(n)
                "up" -> c.up(n)
                "down" -> c.down(n)
                else -> throw IllegalArgumentException("Unrecognized '$line' instruction")
            }
        }

fun partOne(input: String) =
    Coords1().follow(input).location

data class Coords2(
    override val pos: Int = 0,
    override val depth: Int = 0,
    val aim: Int = 0
) : Coords<Coords2> {

    override fun forward(n: Int) =
        copy(pos = pos + n, depth = depth + aim * n)

    override fun up(n: Int) =
        copy(aim = aim - n)

    override fun down(n: Int) =
        copy(aim = aim + n)

}

fun partTwo(input: String) =
    Coords2().follow(input).location

fun partTwoLoop(input: String): Int {
    var pos = 0
    var depth = 0
    var aim = 0
    for (line in input.lineSequence()) {
        val spaceIdx = line.indexOf(" ")
        val n = line.substring(spaceIdx + 1).toInt()
        when (line[0]) {
            'f' -> {
                pos += n
                depth += aim * n
            }
            'u' -> aim -= n
            'd' -> aim += n
            else -> throw IllegalArgumentException("Unrecognized '$line' instruction")
        }
    }
    return pos * depth
}

data class Sub(
    override var pos: Int = 0,
    override var depth: Int = 0,
    var aim: Int = 0
) : OceanLocation {

    fun forward(n: Int) {
        pos += n
        depth += aim * n
    }

    fun up(n: Int) {
        aim -= n
    }

    fun down(n: Int) {
        aim += n
    }

}

fun partTwoStateful(input: String): Int {
    val sub = Sub()
    for (line in input.lineSequence()) {
        val spaceIdx = line.indexOf(" ")
        val n = line.substring(spaceIdx + 1).toInt()
        when (line[0]) {
            'f' -> sub.forward(n)
            'u' -> sub.up(n)
            'd' -> sub.down(n)
            else -> throw IllegalArgumentException("Unrecognized '$line' instruction")
        }
    }
    return sub.location
}