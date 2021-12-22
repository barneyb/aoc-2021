package day19_beacon_scanner

import geom3d.Point
import kotlin.math.abs

/**
 * todo: add notes
 */
fun main() {
    util.solve(::partOne) // 394 is too low
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

fun partOne(input: String): Int {
    val scanners = input.toScanners()
    var totalBeacons = 0
    for ((i, a) in scanners.withIndex()) {
        totalBeacons += a.beaconCount
        for ((j, b) in scanners.withIndex().drop(i + 1)) {
            val summary = mutableMapOf<Beacon, Set<Beacon>>()
            b.deltas.forEach { delta ->
                val matches = a.deltas
                    .filter { it.amag == delta.amag }
                if (matches.isNotEmpty()) {
                    summary.merge(
                        b.beacons[delta.i],
                        matches.map { a.beacons[it.j] }.toSet(),
                        Set<Beacon>::plus
                    )
                    summary.merge(
                        b.beacons[delta.j],
                        matches.map { a.beacons[it.i] }.toSet(),
                        Set<Beacon>::plus
                    )
                }
            }
            if (summary.size >= 12) {
                println("$i -> $j: ${summary.size}")
                totalBeacons -= summary.size
            }
        }
    }
    return totalBeacons
}

fun partTwo(input: String) = input.trim().length
