package day19_beacon_scanner

import geom.Vec
import histogram.count
import histogram.mutableHistogramOf
import java.util.*

/**
 * Another graph walk, reminiscent of Jurassic Jigsaw (2020 day 20), but this
 * time in three dimensions! Based on the example, assume scanner zero's coord
 * system is The One True System, and reorient/position all the scanners based
 * on their shared beacons to those coordinates. Finding the shared beacons has
 * its own pile of fun, as do the coordinate transforms. Goblins and elves? Once
 * the scanners are all sharing a single system, the beacons are trivially
 * plotted in that same system, and then count the unique locations. BAM!
 *
 * Part two is mindless if you did the "whole" solution to part one. If you
 * "cheated" and didn't actually assemble a shared coordinate system, time to do
 * so! Iterate over each pair of scanners and measure is the simple way. There's
 * a tiny bit of room to avoid checking pairs which both aren't on the border,
 * but it doesn't save much with only 30-something scanners.
 */
fun main() {
    util.solve(436, ::partOne) // 394 is too low, 514 is too high
    util.solve(10918, ::partTwo)
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
            scanner.add(
                Vec(
                    l.split(",")
                        .map(String::toLong)
                        .toTypedArray()
                )
            )
        }
    }
    return result
}

fun Collection<Scanner>.relativize(): Collection<Scanner> {
    val first = first()
    first.location = Vec.origin(first.beacons[0].dimensions)
    val queue: Queue<Scanner> = ArrayDeque()
    queue.add(first)
    val theOthers = drop(1)
    while (queue.isNotEmpty()) {
        val scanA = queue.remove()
        for (scanB in theOthers) {
            if (scanB.location != null) continue
            if (scanA == scanB) continue
            repeat(24) {
                val hist = mutableHistogramOf<Vec>() // locations of B
                for (a in scanA.beacons) {
                    for (b in scanB.beacons) {
                        hist.count(Vec(a.x - b.x, a.y - b.y, a.z - b.z))
                    }
                }
                val things = hist.entries
                    .filter { (_, v) -> v >= 12 }
                if (things.isNotEmpty()) {
                    if (things.size > 1)
                        throw IllegalStateException("scanner ${scanB.id} (rel ${scanA.id}) could be at any of ${things.map { it.key }}")
                    val loc = things[0].key + scanA.location!!
                    when (scanB.location) {
                        null -> {
                            scanB.location = loc
                            queue.add(scanB)
                        }
                        loc -> {}
                        else -> throw IllegalStateException("Scanner ${scanB.id} moved to $loc?!")
                    }
                    return@repeat
                }
                scanB.twist()
            }
        }
    }
    return this
}

fun partOne(input: String): Int {
    val scanners = input.toScanners()
        .relativize()

    val allBeacons = HashSet<Vec>()
    scanners.forEach { scanner ->
        scanner.beacons.forEach { beacon ->
            allBeacons.add(beacon + scanner.location!!)
        }
    }

    return allBeacons.size
}

fun partTwo(input: String): Long {
    val scanners = input.toScanners()
        .relativize()
    var max = Long.MIN_VALUE
    for (a in scanners) {
        for (b in scanners) {
            max = max.coerceAtLeast(
                a.location!!.manhattanDistance(b.location!!)
            )
        }
    }
    return max
}
