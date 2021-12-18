package day13_transparent_paper

import geom2d.Point
import geom2d.Rect
import geom2d.toAsciiArt
import kotlin.math.max
import kotlin.math.min

fun main() {
    util.solve(765, ::partOne)
    util.solve(
        """
            ███..████.█..█.████.█....███...██..█..█
            █..█....█.█.█.....█.█....█..█.█..█.█..█
            █..█...█..██.....█..█....█..█.█....████
            ███...█...█.█...█...█....███..█.██.█..█
            █.█..█....█.█..█....█....█....█..█.█..█
            █..█.████.█..█.████.████.█.....███.█..█
        """.trimIndent(),
        ::partTwo
    )
}

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

fun Collection<Point>.toStringGrid() =
    bounds().toAsciiArt {
        if (contains(it)) "█" else "."
    }

fun Collection<Point>.bounds() =
    drop(1).fold(Rect(first(), 1, 1)) { r, p ->
        Rect(
            min(r.x1, p.x),
            min(r.y1, p.y),
            max(r.x2, p.x),
            max(r.y2, p.y),
        )
    }

fun partTwo(input: String): String {
    val (dots, folds) = parse(input)
    return foldItUp(dots, folds).toStringGrid()
}
