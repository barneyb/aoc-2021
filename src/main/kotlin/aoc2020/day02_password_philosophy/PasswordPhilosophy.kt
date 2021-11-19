package aoc2020.day02_password_philosophy

fun main() {
    util.solve(424, ::partOne)
    util.solve(::partTwo)
}

data class Policy(val min: Int, val max: Int, val char: Char) {
    // adding an early-exit when max is exceeded to skip processing the
    // rest of the password makes it slower.
    fun conforms(password: String) =
        password.count { it == char } in min..max
}

data class Record(val policy: Policy, val password: String)

object RecordParser {

    private val RE_RECORD = Regex("""(\d+)-(\d+) ([a-z]): *([a-z]+)""")

    fun parse(it: String): Record = RE_RECORD
        .find(it)
        ?.let { m ->
            Record(
                Policy(
                    m.groups[1]!!.value.toInt(),
                    m.groups[2]!!.value.toInt(),
                    m.groups[3]!!.value.first(),
                ),
                m.groups[4]!!.value
            )
        }
        ?: throw IllegalStateException("Unrecognized record: '$it'")
}

fun partOne(input: String) = input
    .lines()
    .map(RecordParser::parse)
    .count {
        it.policy.conforms(it.password)
    }

fun partTwo(input: String) = input.trim().length
