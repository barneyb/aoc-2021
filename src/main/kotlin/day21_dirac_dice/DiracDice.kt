package day21_dirac_dice

/**
 * todo: add notes
 */
fun main() {
    util.solve(504972, ::partOne)
//    util.solve(::partTwo)
}

data class Player(var pos: Int, var score: Int) {
    fun move(n: Int) {
        pos = (pos - 1 + n) % 10 + 1
        score += pos
    }
}

data class Die(val sides: Int = 100) {
    var rollCount: Int = 0
    val value get() = (rollCount - 1) % sides + 1

    fun roll(): Int {
        rollCount += 1
        return value
    }
}

fun partOne(input: String): Int {
    val (a, b) = input.lines()
        .map { it.split(' ')[4].toInt() }
        .map { Player(it, 0) }
    val die = Die()
    val loser: Player
    while (true) {
        a.move(die.roll() + die.roll() + die.roll())
        if (a.score >= 1000) {
            loser = b
            break
        }
        b.move(die.roll() + die.roll() + die.roll())
        if (b.score >= 1000) {
            loser = a
            break
        }
    }
    return loser.score * die.rollCount
}

fun partTwo(input: String) = input.trim().length
