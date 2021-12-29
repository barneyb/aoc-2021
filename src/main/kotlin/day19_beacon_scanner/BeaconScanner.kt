package day19_beacon_scanner

import geom3d.Point
import histogram.histogramOf
import java.util.*
import kotlin.math.abs

/**
 * todo: add notes
 */
fun main() {
    util.solve(::partOne) // 394 is too low, 514 is too high
    util.solve(::partTwo)
}

data class Delta(
    val i: Int,
    val j: Int,
    val x: Long,
    val y: Long,
    val z: Long
) {
    val mag = listOf(x, y, z).sorted()
    val ax = abs(x)
    val ay = abs(y)
    val az = abs(z)
    val amag = listOf(ax, ay, az).sorted()
}

class Beacon(x: Long, y: Long, z: Long) : Point(x, y, z), Comparable<Beacon> {

    override fun compareTo(other: Beacon) =
        (if (x == other.x)
            if (y == other.y) z - other.z
            else y - other.y
        else x - other.x)
            .let(Long::toInt)
}

class Scanner(val id: Int) {

    var beacons: MutableList<Beacon> = mutableListOf()
    val beaconCount get() = beacons.size

    val deltas: List<Delta> by lazy {
        val deltas = mutableListOf<Delta>()
        for ((i, a) in beacons.withIndex()) {
            for ((j, b) in beacons.withIndex().drop(i + 1)) {
                val delta = Delta(i, j, b.x - a.x, b.y - a.y, b.z - a.z)
                deltas.add(delta)
            }
        }
        deltas
    }

    fun add(beacon: Beacon) =
        beacons.add(beacon)

    override fun toString(): String =
        "Scanner #$id (${beacons.size} beacons)"
}

fun String.toScanners(): List<Scanner> {
    val result = mutableListOf<Scanner>()
    var scanner = Scanner(-1)
    for (l in lines()) {
        if (l.startsWith("--- scanner ")) {
            val idx = l.indexOf(' ', 12)
            scanner = Scanner(l.substring(12, idx).toInt())
            result.add(scanner)
        } else if (l.isNotBlank()) {
            val (x, y, z) = l.split(",").map(String::toLong)
            scanner.add(Beacon(x, y, z))
        }
    }
    result.forEach {
        it.beacons.sort()
    }
    return result
}

private fun <E> SortedSet<E>.plus(other: Iterable<E>) =
    TreeSet(this).apply { addAll(other) }

typealias Summary = Map<Int, SortedSet<Int>>
typealias Overlap = Triple<Int, Int, Summary>

fun partOne(input: String): Int {
    val scanners = input.toScanners()
    val overlaps: Queue<Overlap> = ArrayDeque()
    for ((i, a) in scanners.withIndex()) {
        for ((j, b) in scanners.withIndex().drop(i + 1)) {
            val summary = mutableMapOf<Int, SortedSet<Int>>()
            b.deltas.forEach { n ->
                val matches = a.deltas
                    .filter { m -> m.amag == n.amag }
                if (matches.isNotEmpty()) {
                    summary.merge(
                        n.i,
                        matches.map { it.j }.toSortedSet(),
                        SortedSet<Int>::plus
                    )
                    summary.merge(
                        n.j,
                        matches.map { it.i }.toSortedSet(),
                        SortedSet<Int>::plus
                    )
                }
            }
            if (summary.size >= 12) {
                println(
                    "$i -> $j: ${summary.size} : ${
                        histogramOf(
                            summary.values.map(
                                Collection<*>::size
                            )
                        )
                    }"
                )
                overlaps.add(Triple(i, j, summary))
            }
        }
    }
    val locations = mutableMapOf(0 to Point.ORIGIN)
    while (overlaps.isNotEmpty()) {
        val (i, j, summary) = overlaps.remove()
        if (locations.containsKey(i) && locations.containsKey(j)) {
            // already known - validate
        } else if (locations.containsKey(i)) {
            // j is unknown - compute
            val (m, ns) = summary.entries.first()
            val a = scanners[i].beacons[ns.first()]
            val b = scanners[j].beacons[m]
            val d = 4
        } else if (locations.containsKey(j)) {
            // i is unknown - compute
        } else {
            // neither is known - requeue
            overlaps.add(Triple(i, j, summary))
        }
    }
    return -1
}

fun partTwo(input: String) = input.trim().length
