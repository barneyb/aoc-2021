package aoc2016.day02_bathroom_security

import geom2d.Point
import geom2d.toDir

fun main() {
    util.solve("61529", ::partOne)
    util.solve(::partTwo)
}

internal fun Int.toButton() =
    this.toLong().toButton()

internal fun Long.toButton() =
    Point((this - 1) % 3, (this - 1) / 3)

internal fun Point.isButton() =
    x in 0..2 && y in 0..2

internal fun Point.toButton() =
    1 + y * 3 + x

fun partOne(input: String) = input
    .lines()
    .asSequence()
    .map { it.map(Char::toDir) }
    .runningFold(5.toButton()) { btn, steps ->
        steps.fold(btn) { b, s ->
            val next = b.step(s)
            if (next.isButton()) next else b
        }
    }
    .drop(1) // don't want the start...
    .map(Point::toButton)
    .joinToString(separator = "")

fun partTwo(input: String) = input.trim().length
