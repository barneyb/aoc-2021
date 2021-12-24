package day22_reactor_reboot

import geom.Cuboid

/**
 * Three-space math is easy when it's small! Those huge lines at the end imply
 * it's not going to stay that way, however. But whatever. Little looping, a bit
 * of filtering, a smidge of cringe, and done!
 *
 * todo: add notes
 */
fun main() {
    util.solve(587785, ::partOne)
//    util.solve(::partTwo)
}

sealed class Step(val cuboid: Cuboid)
class On(cuboid: Cuboid) : Step(cuboid)
class Off(cuboid: Cuboid) : Step(cuboid)

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
fun String.toStep(): Step {
    val parts = split(' ')
    val c = parts[1].toCuboid()
    return if (parts[0] == "on") On(c) else Off(c)
}

fun partOne(input: String) =
    HashSet<Cuboid>().also { reactor ->
        Cuboid(-50L..50, -50L..50, -50L..50).also { region ->
            input
                .lines()
                .map(String::toStep)
                .forEach { step ->
                    val box = step.cuboid.intersection(region)
                    if (box.isEmpty()) return@forEach // NEXT!
                    for (x in box.x) {
                        for (y in box.y) {
                            for (z in box.z) {
                                val c = Cuboid(x..x, y..y, z..z)
                                when (step) {
                                    is On -> reactor.add(c)
                                    is Off -> reactor.remove(c)
                                }
                            }
                        }
                    }
                }
        }
    }.size

fun partTwo(input: String) = input.trim().length
