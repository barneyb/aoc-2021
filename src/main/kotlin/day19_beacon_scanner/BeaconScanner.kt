package day19_beacon_scanner

import geom.Vec
import histogram.count
import histogram.mutableHistogramOf
import java.util.*

/**
 * todo: add notes
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
    println("$first - by fiat")
    val queue: Queue<Scanner> = ArrayDeque()
    queue.add(first)
    val theOthers = drop(1)
    while (queue.isNotEmpty()) {
        val scanA = queue.remove()
        for (scanB in theOthers) {
            if (scanA == scanB) continue
            repeat(48) {
                val hist = mutableHistogramOf<Vec>() // locations of B
                for (a in scanA.beacons) {
                    for (b in scanB.beacons) {
                        hist.count(Vec(a.x - b.x, a.y - b.y, a.z - b.z))
                    }
                }
                val things = hist.entries
                    .filter { (k, v) -> v >= 12 }
                if (things.isNotEmpty()) {
                    if (things.size > 1)
                        throw IllegalStateException("scanner ${scanB.id} (rel ${scanA.id}) could be at any of ${things.map { it.key }}")
                    val loc = things[0].key + scanA.location!!
                    when (scanB.location) {
                        null -> {
                            scanB.location = loc
                            println(scanB)
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
