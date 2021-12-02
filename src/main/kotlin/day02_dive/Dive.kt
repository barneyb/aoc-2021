package day02_dive

import geom2d.Dir
import geom2d.Point

fun main() {
    util.solve(1840243, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String): Long {
    val dest = input
        .lineSequence()
        .fold(Point.ORIGIN) { p, it ->
            val words = it.split(" ")
            val n = words[1].toLong()
            when (words[0]) {
                "forward" -> p.step(Dir.EAST, n)
                "up" -> p.step(Dir.NORTH, n)
                "down" -> p.step(Dir.SOUTH, n)
                else -> throw IllegalArgumentException("Unrecognized '$it' instruction")
            }
        }
    return dest.x * dest.y
}

fun partTwo(input: String) = input.trim().length
