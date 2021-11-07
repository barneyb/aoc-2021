package aoc2015.day02_wrap_presents

import kotlin.math.min

fun main() {
    util.solve(1598415, ::partOne)
    util.solve(3812909, ::partTwo)
}

data class Box(val length: Int, val width: Int, val height: Int) {

    companion object {
        fun parse(s: String): Box {
            val dims = s.split("x")
                .map(String::toInt)
            assert(dims.size == 3)
            return Box(dims[0], dims[1], dims[2])
        }
    }

    val frontArea: Int
        get() = width * height

    val topArea: Int
        get() = width * length

    val sideArea: Int
        get() = length * height

    val totalArea: Int
        get() = frontArea * 2 + topArea * 2 + sideArea * 2

    val neededWrappingPaperArea: Int
        get() = totalArea + min(min(frontArea, topArea), sideArea)

    val frontPerim: Int
        get() = 2 * (width + height)

    val topPerim: Int
        get() = 2 * (width + length)

    val sidePerim: Int
        get() = 2 * (length + height)

    val volume: Int
        get() = length * width * height

    val neededRibbonLength: Int
        get() = volume + min(min(frontPerim, topPerim), sidePerim)

}

fun partOne(input: String) = input
    .lines()
    .map(Box::parse)
    .sumOf(Box::neededWrappingPaperArea)

fun partTwo(input: String) = input
    .lines()
    .map(Box::parse)
    .sumOf(Box::neededRibbonLength)
