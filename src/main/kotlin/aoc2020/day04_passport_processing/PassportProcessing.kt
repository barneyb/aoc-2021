package aoc2020.day04_passport_processing

import util.paragraphs

fun main() {
    util.solve(206, ::partOne)
    util.solve(::partTwo)
}

private val requiredKeys =
    setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

fun partOne(input: String) =
    input
        .paragraphs()
        .map {
            it.split(' ')
                .map { it.split(':') }
                .associate { (a, b) -> Pair(a, b) }
        }
        .count { p ->
            requiredKeys.all { k ->
                p.containsKey(k)
            }
        }

fun partTwo(input: String) = input.trim().length
