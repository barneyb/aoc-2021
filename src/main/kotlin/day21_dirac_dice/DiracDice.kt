package day21_dirac_dice

import histogram.Histogram
import histogram.count
import histogram.histogramOf
import histogram.mutableHistogramOf
import kotlin.math.max

/**
 * A simple rules engine is needed: modular arithmetic, a little bookkeeping,
 * and careful attention to boundary conditions in a two-phase loop. Not simple,
 * but straightforward, though rather too straightforward this late in the
 * month. Part two is going to be mind-bending, especially with a quantum
 * physicist as namesake.
 *
 * It's Lanternfish again! Here we need to track counts of game states, a
 * four-tuple of position and score for each player. For each distinct state,
 * split the universe 27 ways on player one, check for victory, then the same
 * for player two, collecting the not-yet-won states up for the next round.
 */
fun main() {
    util.solve(504972, ::partOne)
    util.solve(446968027750017, ::partTwo)
}

data class Player(var pos: Long, var score: Long) {
    fun move(n: Int) {
        pos = (pos - 1 + n) % 10 + 1
        score += pos
    }

    fun split(): Histogram<Player> =
        mutableHistogramOf<Player>().also { hist ->
            (3..9)
                .map { n ->
                    copy().apply { move(n) }
                }
                .zip(listOf(1L, 3, 6, 7, 6, 3, 1))
                .forEach { (p, n) ->
                    hist.count(p, n)
                }
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

fun partOne(input: String): Long {
    val (a, b) = input.toPlayers()
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

private fun String.toPlayers() = lines()
    .map { it.split(' ')[4].toLong() }
    .map { Player(it, 0) }

data class State(val one: Player, val two: Player)

fun partTwo(input: String): Long {
    val (one, two) = input.toPlayers()
    var winsOne = 0L
    var winsTwo = 0L
    var hist = histogramOf(State(one, two))
    while (hist.isNotEmpty()) {
        val next = mutableHistogramOf<State>()
        for ((state, count) in hist) {
            state.one.split().forEach { (o, n) ->
                if (o.score >= 21) {
                    winsOne += count * n
                } else {
                    state.two.split().forEach { (t, m) ->
                        if (t.score >= 21) {
                            winsTwo += count * n * m
                        } else {
                            next.count(State(o, t), count * n * m)
                        }
                    }
                }
            }
        }
        hist = next
    }

    return max(winsOne, winsTwo)
}
