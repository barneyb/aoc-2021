package day06_lanternfish_growth

fun main() {
    util.solve(371379, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) = partOne(input, 80)

fun partOne(input: String, days: Int) =
    (1..days)
        .fold(
            input.split(',')
                .map(String::toInt)
        ) { fish, _ ->
            val next = mutableListOf<Int>()
            fish.forEach {
                when (it) {
                    0 -> {
                        next.add(6) // start
                        next.add(8) // new fish
                    }
                    else -> next.add(it - 1) // decrement
                }
            }
            next
        }
        .size

fun partTwo(input: String) = input.trim().length
