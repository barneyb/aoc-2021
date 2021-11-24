package aoc2015.day06_fire_hazard

import geom2d.Point
import java.util.*

fun main() {
    util.solve(543903, ::partOne)
    util.solve(14687245, ::partTwo)
}

private fun String.toPoint(): Point {
    val dims = split(",")
        .map { it.toLong() }
    return Point(dims[0], dims[1])
}

private fun gridRanges(a: Point, b: Point): Collection<IntRange> {
    return (a.y..b.y).map {
        val offset = 1_000 * it
        (a.x + offset).toInt()..(b.x + offset).toInt()
    }
}

private data class Op(val name: String, val ranges: Collection<IntRange>) {

    fun execute(work: (IntRange) -> Unit) {
        ranges.forEach(work)
    }

}

private fun String.toOp(): Op {
    var words = split(" ")
    if (words.first() == "turn") {
        words = words.subList(1, words.size)
    }
    val ranges = gridRanges(
        words[1].toPoint(),
        words[3].toPoint(),
    )
    return Op(words.first(), ranges)
}

fun partOne(input: String): Int {
    val lights = BitSet(1_000_000)
    input.lines()
        .map { it.toOp() }
        .forEach { op ->
            op.execute(
                when (op.name) {
                    "on" -> { it -> lights.set(it.first, it.last + 1) }
                    "off" -> { it -> lights.clear(it.first, it.last + 1) }
                    "toggle" -> { it -> lights.flip(it.first, it.last + 1) }
                    else -> throw IllegalArgumentException("unrecognized instruction '$op'")
                }
            )
        }
    return lights.cardinality()
}

fun partTwo(input: String): Int {
    val lights = ShortArray(1_000_000)
    input.lines()
        .map { it.toOp() }
        .forEach { op ->
            op.execute(
                when (op.name) {
                    "on" -> { it ->
                        it.forEach { l ->
                            // this is just stupid
                            lights[l] = (lights[l] + 1).toShort()
                        }
                    }
                    "off" -> { it ->
                        it.forEach { l ->
                            if (lights[l] > 0)
                                lights[l] = (lights[l] - 1).toShort()
                        }
                    }
                    "toggle" -> { it ->
                        it.forEach { l ->
                            lights[l] = (lights[l] + 2).toShort()
                        }
                    }
                    else -> throw IllegalArgumentException("unrecognized instruction '$op'")
                }
            )
        }
    return lights.sum()
}
