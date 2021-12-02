package day02_dive

fun main() {
    util.solve(1840243, ::partOne)
    util.solve(1727785422, ::partTwo)
}

fun partOne(input: String): Int {
    val dest = input
        .lineSequence()
        .fold(Pair(0, 0)) { p, line ->
            val words = line.split(" ")
            val n = words[1].toInt()
            when (words[0]) {
                "forward" -> Pair(p.first + n, p.second)
                "up" -> Pair(p.first, p.second - n)
                "down" -> Pair(p.first, p.second + n)
                else -> throw IllegalArgumentException("Unrecognized '$line' instruction")
            }
        }
    return dest.first * dest.second
}

fun partTwo(input: String): Int {
    val dest = input
        .lineSequence()
        .fold(Triple(0, 0, 0)) { t, line ->
            val words = line.split(" ")
            val n = words[1].toInt()
            when (words[0]) {
                "forward" -> Triple(
                    t.first + n,
                    t.second + t.third * n,
                    t.third
                )
                "up" -> Triple(t.first, t.second, t.third - n)
                "down" -> Triple(t.first, t.second, t.third + n)
                else -> throw IllegalArgumentException("Unrecognized '$line' instruction")
            }
        }
    return dest.first * dest.second
}
