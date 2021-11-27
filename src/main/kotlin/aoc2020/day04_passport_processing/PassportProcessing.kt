package aoc2020.day04_passport_processing

import util.paragraphs

fun main() {
    util.solve(206, ::partOne)
    util.solve(123, ::partTwo)
}

private fun String.toKeyMap() =
    paragraphs()
        .map {
            it.split(' ')
                .map { it.split(':') }
                .associate { (a, b) -> Pair(a, b) }
        }

private val EYE_COLORS =
    hashSetOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
private val RE_HEX_COLOR = "^#[0-9a-f]{6}$".toRegex()
private val RE_PASSPORT_ID = "^[0-9]{9}$".toRegex()

private val validators = mapOf<String, (String) -> Boolean>(
    "byr" to { it.toInt() in 1920..2002 },
    "iyr" to { it.toInt() in 2010..2020 },
    "eyr" to { it.toInt() in 2020..2030 },
    "hgt" to {
        when (it.substring(it.length - 2)) {
            "cm" -> it.substring(0, it.length - 2).toInt() in 150..193
            "in" -> it.substring(0, it.length - 2).toInt() in 59..76
            else -> false
        }
    },
    "hcl" to { RE_HEX_COLOR.matches(it) },
    "ecl" to { it in EYE_COLORS },
    "pid" to { RE_PASSPORT_ID.matches(it) },
)

fun partOne(input: String) =
    input
        .toKeyMap()
        .count { p ->
            validators.keys.all { k ->
                p.containsKey(k)
            }
        }

fun partTwo(input: String) =
    input
        .toKeyMap()
        .count { p ->
            validators.all { (k, validator) ->
                val v = p[k]
                v != null && validator(v)
            }
        }
