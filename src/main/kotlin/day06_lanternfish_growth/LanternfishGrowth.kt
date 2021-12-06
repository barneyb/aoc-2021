package day06_lanternfish_growth

fun main() {
    util.solve(371379, ::partOne)
    util.solve(1674303997472, ::partTwo)
}

fun partOne(input: String) = simulate(input, 80)

fun simulate(input: String, days: Int): Long {
    val initial = mutableMapOf<Int, Long>()
    input.split(',')
        .map(String::toInt)
        .forEach { initial.merge(it, 1, Long::plus) }
    return (1..days)
        .fold(initial) { fish, day ->
            val next = mutableMapOf<Int, Long>()
            fish.entries.forEach { (timer, count) ->
                when (timer) {
                    0 -> {
                        next.merge(6, count, Long::plus) // reset
                        next.merge(8, count, Long::plus) // birth
                    }
                    else ->
                        next.merge(timer - 1, count, Long::plus) // decrement
                }
            }
            next
        }
        .values
        .sum()
}

fun partTwo(input: String) = simulate(input, 256)
