package day24_arithmetic_logic_unit

/**
 * todo: add notes
 */
fun main() {
    util.solve(51983999947999, ::partOne) // 51993999947999 is too high
//    util.solve(::partTwo)
}

sealed class Op(val register: String) {
    override fun toString(): String {
        return "${javaClass.simpleName}(register='$register')"
    }
}

class Inp(register: String) : Op(register)
abstract class BinOp(register: String, val arg: String) : Op(register) {
    fun isLiteral() = arg[0] == '-' || arg[0] in '0'..'9'
    val value get() = arg.toLong()
    override fun toString(): String {
        return "${javaClass.simpleName}(register='$register', arg='$arg')"
    }
}

class Add(register: String, arg: String) : BinOp(register, arg)
class Assign(register: String, arg: String) : BinOp(register, arg)
class Sub(register: String, arg: String) : BinOp(register, arg)
class Mul(register: String, arg: String) : BinOp(register, arg)
class Div(register: String, arg: String) : BinOp(register, arg)
class Mod(register: String, arg: String) : BinOp(register, arg)
class Eql(register: String, arg: String) : BinOp(register, arg)
class Neq(register: String, arg: String) : BinOp(register, arg)


class Machine() {
    var w: Long = 0
    var x: Long = 0
    var y: Long = 0
    var z: Long = 0

    private fun parse(program: String): List<Op> {
        val ops = program.lines()
            .map {
                val parts = it.split(' ')
                val a = parts[1]
                if (parts[0] == "inp") {
                    Inp(a)
                } else {
                    val b = parts[2]
                    when (parts[0]) {
                        "add" -> Add(a, b)
                        "mul" -> Mul(a, b)
                        "div" -> Div(a, b)
                        "mod" -> Mod(a, b)
                        "eql" -> Eql(a, b)
                        else -> throw IllegalArgumentException("Unknown '${parts[0]}' instruction")
                    }
                }
            }.toMutableList()
//        var i = 1 // 0 is always read
//        while (i < ops.size) {
//            val prev = ops[i - 1]
//            val op = ops[i]
//            if (op is Add && prev is Mul && prev.isLiteral() && prev.value == 0L) {
//                ops[i] = Set(op.register, op.arg)
//                ops.removeAt(--i)
//            } else if (op is Add && op.isLiteral() && op.value < 0) {
//                ops[i] = Sub(op.register, (op.value * -1).toString())
//            } else if (op is Eql && op.isLiteral() && op.value == 0L && prev is Eql && op.register == prev.register) {
//                ops[i] = Neq(op.register, prev.arg)
//                ops.removeAt(--i)
//            } else {
//                ++i
//            }
//        }
        return ops
    }

    fun execute(program: String) {
        execute(program, emptyList())
    }

    fun execute(program: String, input: List<Long>) {
        execute(this.parse(program), input)
    }

    fun execute(program: List<Op>, input: List<Long>) {
        var inputPointer = 0
        program.forEach {
            when (it) {
                is Inp -> {
                    println(z)
                    set(it.register, input[inputPointer++])
                }
                is Add -> set(it.register, get(it.register) + get(it.arg))
                is Div -> set(it.register, get(it.register) / get(it.arg))
                is Mod -> set(it.register, get(it.register) % get(it.arg))
                is Mul -> set(it.register, get(it.register) * get(it.arg))
                is Sub -> set(it.register, get(it.register) - get(it.arg))
                is Assign -> set(it.register, get(it.arg))
                is Eql -> set(
                    it.register,
                    if (get(it.register) == get(it.arg)) 1 else 0
                )
                is Neq -> set(
                    it.register,
                    if (get(it.register) != get(it.arg)) 1 else 0
                )
                else -> throw IllegalArgumentException("Unknown $it")
            }
//        println("$it => ($w, $x, $y, $z)")
            //                    println("${get(it.register)} == ${get(it.arg)}?")
            //                    println("${get(it.register)} == ${get(it.arg)}?")
        }
    }

    fun translate(program: String): String {
        val result = mutableListOf<String>()
        val ops = parse(program).toMutableList()
        var i = 0
        while (i < ops.size) {
            val op = ops[i]
            if (op is Inp) {
                result.add("${op.register} = read()")
                ++i
                continue
            }
            result.add(
                when (op) {
                    is Add -> "${op.register} += ${op.arg}"
                    is Div -> "${op.register} /= ${op.arg}"
                    is Mod -> "${op.register} %= ${op.arg}"
                    is Mul -> "${op.register} *= ${op.arg}"
                    is Sub -> "${op.register} -= ${op.arg}"
                    is Assign -> {
                        var expr = op.arg
                        while (i < ops.size - 1) {
                            val next = ops[i + 1]
                            if (next is Mul && next.register == op.register) {
                                if (expr.contains(' ')) {
                                    expr = "($expr)"
                                }
                                expr += " * ${next.arg}"
                                ops.removeAt(i)
                            } else if (next is Mod && next.register == op.register) {
                                if (expr.contains(' ')) {
                                    expr = "($expr)"
                                }
                                expr += " % ${next.arg}"
                                ops.removeAt(i)
                            } else if (next is Add && next.register == op.register) {
                                expr += " + ${next.arg}"
                                ops.removeAt(i)
                            } else {
                                break
                            }
                        }
                        "${op.register} = ${expr}"
                    }
                    is Eql -> "${op.register} = if (${op.register} == ${op.arg}${if (op.isLiteral()) "L" else ""}) 1 else 0"
                    is Neq -> "${op.register} = if (${op.register} != ${op.arg}${if (op.isLiteral()) "L" else ""}) 1 else 0"
                    else -> throw IllegalArgumentException("Unknown $op")
                }
            )
            ++i
        }
        return result
            .filter { it.isNotBlank() }
            .joinToString(separator = "\n")
    }

    private fun get(regOrVal: String) =
        when (regOrVal[0]) {
            'w' -> w
            'x' -> x
            'y' -> y
            'z' -> z
            '-', in '0'..'9' -> regOrVal.toLong()
            else -> throw IllegalArgumentException("Unknown '$regOrVal' register.")
        }

    private fun set(register: String, value: Long) {
        when (register) {
            "w" -> w = value
            "x" -> x = value
            "y" -> y = value
            "z" -> z = value
            else -> throw IllegalArgumentException("Unknown '$register' register.")
        }
    }
}

/*
inp w
mul x    0   0   0   0   0   0   0   0   0   0   0   0   0   0
add x    z   z   z   z   z   z   z   z   z   z   z   z   z   z
mod x   26  26  26  26  26  26  26  26  26  26  26  26  26  26
div z    1   1   1  26   1  26  26   1  26   1   1  26  26  26
add x   11  11  15 -14  10   0  -6  13  -3  13  15  -2  -9  -2
eql x    w   w   w   w   w   w   w   w   w   w   w   w   w   w
eql x    0   0   0   0   0   0   0   0   0   0   0   0   0   0
mul y    0   0   0   0   0   0   0   0   0   0   0   0   0   0
add y   25  25  25  25  25  25  25  25  25  25  25  25  25  25
mul y    x   x   x   x   x   x   x   x   x   x   x   x   x   x
add y    1   1   1   1   1   1   1   1   1   1   1   1   1   1
mul z    y   y   y   y   y   y   y   y   y   y   y   y   y   y
mul y    0   0   0   0   0   0   0   0   0   0   0   0   0   0
add y    w   w   w   w   w   w   w   w   w   w   w   w   w   w
add y    6  14  13   1   6  13   6   3   8  14   4   7  15   1
mul y    x   x   x   x   x   x   x   x   x   x   x   x   x   x
add z    y   y   y   y   y   y   y   y   y   y   y   y   y   y
 */

fun doRound(z: Long, r: Int, w: Long) =
    doRound(z, A[r], B[r], C[r], w)

fun doRound(z: Long, a: Long, b: Long, c: Long, w: Long) =
    if (z % 26 + b == w)
        z / a
    else
        z / a * 26 + w + c

val RE_TOKENIZE = " +".toRegex()
val A = "1   1   1  26   1  26  26   1  26   1   1  26  26  26"
    .split(RE_TOKENIZE)
    .map(String::toLong)
    .toLongArray()
val B = "11  11  15 -14  10   0  -6  13  -3  13  15  -2  -9  -2"
    .split(RE_TOKENIZE)
    .map(String::toLong)
    .toLongArray()
val C = "6  14  13   1   6  13   6   3   8  14   4   7  15   1"
    .split(RE_TOKENIZE)
    .map(String::toLong)
    .toLongArray()

fun searchIn(r: Int, targets: Set<Long>): Set<Long> {
    val next = mutableSetOf<Long>()
    for (z in -1_000_000..1_000_000L)
        findWs(z, r, targets, next)
    inputZByRound[r] = next
    return next
}

private val inputZByRound = mutableMapOf<Int, Set<Long>>()

private fun findWs(
    z: Long,
    r: Int,
    targets: Set<Long>,
    next: MutableSet<Long>
) {
    for (w in 9 downTo 1L) {
        val v = doRound(z, A[r], B[r], C[r], w)
        if (targets.contains(v)) {
            next.add(z)
//            println("r=$r, z=$z, w=$w yields $v")
        }
    }
}

fun partOne(program: String): Long {
    var targets = setOf(0L)
    for (r in 13 downTo 0) {
        if (targets.isEmpty()) {
            println("no targets for round $r")
            break
        }
        targets = searchIn(r, targets)
    }

    fun walk(r: Int, z: Long, digits: List<Long>): Long {
        if (digits.size == 14) {
            return digits.fold(0L) { agg, n ->
                agg * 10 + n
            }
        }
        val targets = if (r == 13) setOf(0L)
        else inputZByRound[r + 1]!!
        for (w in 9 downTo 1L) {
            val v = doRound(z, r, w)
            if (targets.contains(v)) {
                val answer = walk(r + 1, v, digits + w)
                if (answer > 0) {
                    return answer
                }
            }
        }
        return -1
    }
    // for round zero, the input z MUST be zero
    // searching for round thirteen w/ output z of zero
    return walk(0, 0, emptyList())
}

fun partOne_old(program: String): Long {
    val W = longArrayOf(2, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
//    val m = Machine()
//    m.execute(program, W.toList())
//    println(m.z)
    println("=".repeat(80))
//    util.benchmark(104687780) {
    var z = 0L
    (0..13).forEach { i ->
//        println(z)
        val s = doRound(z, A[i], B[i], C[i], W[i])

        if (z % 26 + B[i] != W[i]) {
            z = z / A[i] * 26L
            z += (W[i] + C[i])
        } else {
            z /= A[i]
        }

        if (s != z) {
            throw IllegalStateException("Got $s, expected $z")
        }
    }
//        z
    println(z)
//    }
//    val slices = program
//        .split("inp")
//        .filter { it.isNotBlank() }
//        .map { "inp$it".trim() }
//    val m = Machine()
//    slices
//        .forEachIndexed { idx, prgm ->
//            println(
//                """
//                fun slice_$idx(priorZ: Long, w:Long):Long {
//                    var x: Long
//                    var y: Long
//                    var z = priorZ
//                """.trimIndent()
//            )
//            println(m.translate(prgm)
//                .prependIndent("    ").lines().drop(1).joinToString(separator = "\n"))
//            println("""
//                    return z
//                }
//            """.trimIndent())
////            (1..9L).forEach {
////                val m = Machine()
////                m.execute(prgm, listOf(it))
////                println("  $it -> (${m.w}, ${m.x}, ${m.y}, ${m.z})")
////            }
//        }

    fun search(goal: Long, slice: (Long, Long) -> Long): Map<Long, List<Long>> {
        val zs = mutableMapOf<Long, MutableList<Long>>()
        for (w in 9 downTo 1L) {
            // input z will yield an output z of zero, for the largest w?
            for (z in -10000..10000L) {
                if (slice(z, w) == goal) {
                    zs.getOrPut(w, ::mutableListOf).add(z)
                }
            }
        }
        return zs
    }




    return -1
}

fun partTwo(input: String) = input.trim().length
