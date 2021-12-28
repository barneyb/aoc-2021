package day24_arithmetic_logic_unit

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
