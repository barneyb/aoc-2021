package day13_transparent_paper

import geom2d.Point

fun main() {
    util.solve(765, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String): Int {
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
    return folds
        .take(1)
        .fold(dots) { ds, (a, n) ->
            when (a) {
                'x' -> ds.map { p ->
                    if (p.x <= n) p
                    else p.copy(x = n - (p.x - n))
                }
                'y' -> ds.map { p ->
                    if (p.y > n) p
                    else p.copy(y = n - (p.y - n))
                }
                else -> throw IllegalArgumentException("Unknown fold along '$a'?!")
            }
                .toSet()
        }
        .size
}

fun partTwo(input: String) = input.trim().length
