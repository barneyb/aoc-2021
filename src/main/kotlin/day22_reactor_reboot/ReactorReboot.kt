package day22_reactor_reboot

import geom.Cuboid

/**
 * Three-space math is easy when it's small! Those huge lines at the end imply
 * it's not going to stay that way, however. But whatever. Little looping, a bit
 * of filtering, a smidge of cringe, and done!
 *
 * Part two is simply "remove the 100x100x100 constraint". This requires a
 * smarter approach: treating the cuboids as the entity of interest not the
 * cubes themselves. As cuboids turn on and off, subdivide them as necessary to
 * track cuboids that are entirely on. Once all the steps are completed, compute
 * the number of cubes in each lit cuboid and add them up.
 */
fun main() {
    util.solve(587785, ::partOne)
    util.solve(1167985679908143, ::partTwo)
}

data class Step(val on: Boolean, val cuboid: Cuboid)

// x=10..12,y=10..12,z=10..12
fun String.toCuboid(): Cuboid {
    val (x, y, z) = split(',')
        .map { dim ->
            val (a, b) = dim.split('=')[1]
                .split("..")
                .map(String::toLong)
            a..b
        }
    return Cuboid(x, y, z)
}

// on x=10..12,y=10..12,z=10..12
fun String.toStep(region: Cuboid? = null): Step {
    val parts = split(' ')
    var c = parts[1].toCuboid()
    if (region != null) {
        c = c.intersection(region)
    }
    return Step(parts[0] == "on", c)
}

fun partOne(input: String) =
    Cuboid(-50L..50, -50L..50, -50L..50).let { region ->
        solve(input
            .lines()
            .map { it.toStep(region) })
    }

private fun solve(steps: List<Step>) =
    steps
        .fold(HashSet<Cuboid>()) { reactor, step ->
            val next = HashSet<Cuboid>(reactor)
            if (step.on) {
                next.add(step.cuboid)
            }
            reactor.forEach { existing ->
                val surg = performSurgery(existing, step.cuboid)
                if (!surg.isNoop) {
                    next.remove(existing)
                    next.addAll(surg.fromOne)
                }
            }
            next
        }
        .sumOf(Cuboid::size)

fun partTwo(input: String) =
    solve(
        input
            .lines()
            .map(String::toStep)
    )
