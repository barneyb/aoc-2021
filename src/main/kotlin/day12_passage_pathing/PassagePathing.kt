package day12_passage_pathing

import java.util.*

fun main() {
    util.solve(5333, ::partOne)
    util.solve(146553, ::partTwo)
}

//typealias Cave = String
//
//private const val START: Cave = "start"
//private const val END: Cave = "end"
//
//val Cave.isUnlimitedEntry
//    get() =
//        this[0].isUpperCase()

typealias Cave = Int

private const val START: Cave = 0
private const val END: Cave = 1

val Cave.isUnlimitedEntry
    get() =
        this > END

data class Path(val at: Cave, val visited: List<Cave>, val double: Boolean) {

    constructor(at: Cave) : this(at, listOf(at), false)

    fun then(cave: Cave): Path {
        return Path(
            cave,
            visited + cave,
            double || (!cave.isUnlimitedEntry && visited.contains(cave))
        )
    }

}

private fun String.toGraph(): Map<Cave, Collection<Cave>> {
    val nodes = mutableMapOf("start" to START, "end" to END)
    val graph: Map<Cave, Collection<Cave>> =
        mutableMapOf<Cave, MutableList<Cave>>()
            .apply {
                lines()
                    .map { edge ->
                        edge.split('-')
                            .map { cave ->
                                nodes.getOrPut(cave) {
                                    if (cave[0].isUpperCase())
                                        nodes.size
                                    else
                                        -nodes.size
                                }
                            }
                    }
                    .map { (a, b) ->
                        getOrPut(a, ::mutableListOf).add(b)
                        getOrPut(b, ::mutableListOf).add(a)
                    }
            }
//    println(nodes)
    return graph
}

private fun countPaths(input: String, accept: (Path, Cave) -> Boolean): Int {
    val graph = input.toGraph()
    var pathCount = 0
//    val paths = mutableSetOf<Path>()
    val queue: Queue<Path> = ArrayDeque()
    queue.add(Path(START))
    while (queue.isNotEmpty()) {
        val p = queue.remove()
        if (p.at == END) {
            pathCount += 1
//            println(p)
//            if (!paths.add(p)) throw IllegalStateException("Duplicate path recorded?! ${p}")
//            if (paths.size > 40) throw IllegalStateException("runaway traversal...")
            continue
        }
        queue.addAll(
            graph[p.at]!!
                .filter { accept(p, it) }
                .map { p.then(it) }
        )
    }
    return pathCount
//    return paths.size
}

fun partOne(input: String) =
    countPaths(input) { p: Path, it: Cave ->
        it.isUnlimitedEntry || !p.visited.contains(it)
    }

fun partTwo(input: String) =
    countPaths(input) { p: Path, it: Cave ->
        if (it.isUnlimitedEntry)
            true
        else if (it == START)
            false
        else
            !p.double || !p.visited.contains(it)
    }
