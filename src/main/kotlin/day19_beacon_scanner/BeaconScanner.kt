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
//    util.solve(::partTwo)
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

fun partOne(input: String): Int {
    val scanners = input.toScanners()
    scanners[0].location = Vec.origin(scanners[0].beacons[0].dimensions)
    val queue: Queue<Scanner> = ArrayDeque()
    queue.add(scanners.first())
    val theOthers = scanners.drop(1).toMutableSet()
    while (queue.isNotEmpty()) {
        val scanA = queue.remove()
        println("look for ${scanA.id} overlapping w/ ${theOthers.map(Scanner::id)}")
        for (scanB in theOthers) {
            if (scanA == scanB) continue
            println("  ${scanB.id}...")
            for (garbage in 1..48) {
                val hist = mutableHistogramOf<Vec>() // locations of B
                for ((idxBeaconA, a) in scanA.beacons.withIndex()) {
                    for ((idxBeaconB, b) in scanB.beacons.withIndex()) {
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
                            queue.add(scanB)
                            println("    at $loc!")
                        }
                        loc -> println("    still at $loc!")
                        else -> throw IllegalStateException("Scanner ${scanB.id} moved to $loc?!")
                    }
                    break
                }
                scanB.twist()
            }
        }
    }

    val allBeacons = HashSet<Vec>()
    scanners.forEach { scanner ->
        println("${scanner.id.toString().padStart(2)}: ${scanner.location}")
        scanner.beacons.forEach { beacon ->
            allBeacons.add(beacon + scanner.location!!)
        }
    }

    return allBeacons.size
}

fun partTwo(input: String) = input.trim().length
