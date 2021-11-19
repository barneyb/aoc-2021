package aoc2020.day02_password_philosophy

fun main() {
    util.solve(424, ::partOne)
    util.solve(747, ::partTwo)
}

data class Policy(val a: Int, val b: Int, val char: Char)

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

private fun String.toRecords() = lines()
    .map(RecordParser::parse)

fun partOne(input: String): Int {
    // adding an early-exit when max is exceeded to skip processing
    // the rest of the password makes it slower.
    fun Policy.conforms(password: String) =
        password.count { it == char } in a..b

    return input
        .toRecords()
        .count {
            it.policy.conforms(it.password)
        }
}

fun partTwo(input: String): Int {
    fun Policy.conforms(password: String): Boolean {
        val atA = password[a - 1] == char
        val atB = password[b - 1] == char
        return if (atA) !atB else atB
    }

    return input
        .toRecords()
        .count {
            it.policy.conforms(it.password)
        }
}
