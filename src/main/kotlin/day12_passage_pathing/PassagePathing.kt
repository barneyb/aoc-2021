package day12_passage_pathing

import java.util.*

fun main() {
    util.solve(5333, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String): Int {
    // this is sorta gross. encode the strings!
    val graph: Map<String, List<String>> =
        mutableMapOf<String, MutableList<String>>()
            .apply {
                input
                    .lines()
                    .map { it.split('-') }
                    .map { (a, b) ->
                        getOrPut(a, ::mutableListOf).add(b)
                        getOrPut(b, ::mutableListOf).add(a)
                    }
            }

    data class Path(val at: String, val visited: Set<String>) {

        constructor(at: String) : this(at, setOf(at))

        fun then(cave: String) =
            Path(cave, visited + cave)

    }

    var pathCount = 0
    val queue: Queue<Path> = LinkedList()
    queue.add(Path("start"))
    while (queue.isNotEmpty()) {
        val p = queue.remove()
        if (p.at == "end") {
            pathCount += 1
            continue
        }
        queue.addAll(
            graph[p.at]!!
                .filter { it[0].isUpperCase() || !p.visited.contains(it) }
                .map { p.then(it) }
        )
    }
    return pathCount
}

fun partTwo(input: String) = input.trim().length
