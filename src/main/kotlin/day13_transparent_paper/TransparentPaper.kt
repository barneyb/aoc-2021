package day13_transparent_paper

import geom2d.Point
import geom2d.toStringGrid

/**
 * More 2D fun, this time transforming the plane itself, not moving around it.
 * It's also got another heterogeneous input file, though this one's simpler
 * than Squid Bingo (#4) was. More subtle, representing the plane here is a bad
 * choice; only the dots are interesting and a cursory look at the input says
 * the dots are pretty sparse.
 *
 * Oof. Print and _manually_ read some letters? With the first fold done for
 * part one, iterating the rest is simple. Another "Game of Life"-style
 * immutable fold is reasonable, but re-projecting into the same set and only
 * reducing the bounding box would also work. Either way, the second part
 * requires drawing the resulting "ASCII art" so you can read the answer.
 */
fun main() {
    util.solve(765, ::partOne)
    util.solve("RZKZLPGH", ::partTwo)
}

private val GITHUB_ART = """
    ███..████.█..█.████.█....███...██..█..█
    █..█....█.█.█.....█.█....█..█.█..█.█..█
    █..█...█..██.....█..█....█..█.█....████
    ███...█...█.█...█...█....███..█.██.█..█
    █.█..█....█.█..█....█....█....█..█.█..█
    █..█.████.█..█.████.████.█.....███.█..█
""".trimIndent()
private val GOOGLE_ART = """
    ███..████.████.█..█.███...██..████.███.
    █..█....█.█....█..█.█..█.█..█.█....█..█
    █..█...█..███..████.█..█.█..█.███..█..█
    ███...█...█....█..█.███..████.█....███.
    █....█....█....█..█.█.█..█..█.█....█.█.
    █....████.████.█..█.█..█.█..█.████.█..█
""".trimIndent()

typealias Fold = Pair<Char, Int>

private fun parse(input: String): Pair<Set<Point>, List<Fold>> {
    val (dotSpecs, foldSpecs) = input
        .split("\n\n")
    val dots = dotSpecs
        .lineSequence()
        .map {
            it.split(',')
                .map(String::toLong)
        }
        .map { Point(it.first(), it.last()) }
        .toSet()
    val folds = foldSpecs
        .lines()
        .map {
            it
                .split(' ')[2]
                .split('=')
        }
        .map { Pair(it.first()[0], it.last().toInt()) }
    return Pair(dots, folds)
}

fun partOne(input: String): Int {
    val (dots, folds) = parse(input)
    return foldItUp(dots, folds.take(1)).size
}

private fun foldItUp(dots: Set<Point>, folds: List<Fold>): Set<Point> {
    return folds.fold(dots) { ds, (a, n) ->
        when (a) {
            'x' -> ds.map { p ->
                if (p.x <= n) p
                else p.copy(x = n - (p.x - n))
            }
            'y' -> ds.map { p ->
                if (p.y <= n) p
                else p.copy(y = n - (p.y - n))
            }
            else -> throw IllegalArgumentException("Unknown fold along '$a'?!")
        }
            .toSet()
    }
}

fun partTwo(input: String): String {
    val (dots, folds) = parse(input)
    val art = foldItUp(dots, folds).toStringGrid()
    return when (art) {
        GITHUB_ART -> "RZKZLPGH"
        GOOGLE_ART -> "PZEHRAER"
        else -> art
    }
}
