package day12_passage_pathing

import java.util.*

fun main() {
    util.solve(5333, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String): Int {
    val START = 0
    val END = 1
    val nodes = mutableMapOf("start" to START, "end" to END)
    val graph: Map<Int, Collection<Int>> =
        mutableMapOf<Int, MutableList<Int>>()
            .apply {
                input
                    .lines()
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

    data class Path(val at: Int, val visited: Set<Int>) {

        constructor(at: Int) : this(at, setOf(at))

        fun then(cave: Int) =
            Path(cave, visited + cave)

    }

    var pathCount = 0
    val queue: Queue<Path> = ArrayDeque()
    queue.add(Path(START))
    while (queue.isNotEmpty()) {
        val p = queue.remove()
        if (p.at == END) {
            pathCount += 1
            continue
        }
        queue.addAll(
            graph[p.at]!!
                .filter { it > 0 || !p.visited.contains(it) }
                .map { p.then(it) }
        )
    }
    return pathCount
}

fun partTwo(input: String) = input.trim().length
