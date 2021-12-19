package day18_snailfish

/**
 * todo: add notes
 */
fun main() {
    util.solve(::partOne)
    util.solve(::partTwo)
}

private data class IntAt(val value: Int, val start: Int, val end: Int) {
    val length get() = end - start
}

private fun CharSequence.intAt(i: Int): IntAt {
    var end = i + 1
    while (end < length && this[end].isDigit()) end++
    return IntAt(substring(i, end).toInt(), i, end)
}

fun String.snailExplode(): String {
    var depth = 0
    var idxToExplode = -1
    for (i in this.indices) {
        val c = this[i]
        if (c == '[') depth++
        else if (c == ']') --depth
        else if (depth > 4) {
            idxToExplode = i
            break
        }
    }
    if (idxToExplode < 0) return this
    val left = intAt(idxToExplode)
    val right = intAt(left.end + 1/* comma */)
    val sb = StringBuilder(this)
    fun addAtIndex(idx: Int, other: IntAt) {
        val it = sb.intAt(idx)
        sb.replace(
            it.start,
            it.end,
            (it.value + other.value).toString()
        )
    }
    // do these right-to-left so indexes don't change
    for (i in right.end + 1 until length) {
        if (this[i] in '0'..'9') {
            addAtIndex(i, right)
            break
        }
    }
    sb.replace(
        left.start - 1/* open brace */,
        right.end + 1 /* close brace */,
        "0"
    )
    for (i in idxToExplode - 1 downTo 0) {
        if (this[i] in '0'..'9') {
            // and seek back to start of number...
            var j = i - 1
            while (this[j] in '0'..'9') j--
            addAtIndex(j + 1, left)
            break
        }
    }
    return sb.toString()
}

fun String.snailSplit(): String {
    for (i in 0 until length - 1) {
        if (this[i] in '0'..'9' && this[i + 1] in '0'..'9') {
            val it = intAt(i)
            val a = it.value / 2
            val b = it.value - a
            return replaceRange(it.start, it.end, "[$a,$b]")
        }
    }
    return this
}

fun String.snailReduce(): String {
    var curr = this
//    println("reducing: $this")
    while (true) {
        var next = curr.snailExplode()
        if (next != curr) {
//            println(" explode: $next")
            curr = next
            continue
        }
        next = curr.snailSplit()
        if (next != curr) {
//            println("   split: $next")
            curr = next
            continue
        }
//        println("reduced : $next")
        return next // done!
    }
}

fun String.snailPlus(other: String): String {
    val result = "[$this,$other]".snailReduce()
//    println("\n  $this\n+ $other\n= $result\n")
    return result
}

val String.snailMagnitude: Int
    get() {
        TODO("Not yet implemented")
    }

/*
reducing: [[[[[6,6],[ 6,6]],[[ 6,0],[6,7]]],[[[ 7,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[  0  ,[12,6]],[[ 6,0],[6,7]]],[[[ 7,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,   0  ],[[12,0],[6,7]]],[[[ 7,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   0  ,[6,7]]],[[[ 7,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   6  ,  0  ]],[[[14,7],[ 8,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   6  , 14  ]],[[   0  ,[15,9]],[ 8,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   6  , 14  ]],[[  15  ,   0  ],[17,[8,1]]]],[2,9]]
 explode: [[[[ 12  ,  12  ],[   6  , 14  ]],[[  15  ,   0  ],[115,0]]],[3,9]]
   split: [[[[[6,6],12],[6,14]],[[15,0],[115,0]]],[3,9]]
 explode: [[[[0,18],[6,14]],[[15,0],[115,0]]],[3,9]]
   split: [[[[0,[9,9]],[6,14]],[[15,0],[115,0]]],[3,9]]
 */

fun partOne(input: String) = input.trim().length

fun partTwo(input: String) = input.trim().length
