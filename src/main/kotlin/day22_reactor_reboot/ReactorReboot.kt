package day22_reactor_reboot

/**
 * Three-space math is easy when it's small! Those huge lines at the end imply
 * it's not going to stay that way, however. But whatever. Little looping, a bit
 * of filtering, a smidge of cringe, and done!
 */
fun main() {
    util.solve(587785, ::partOne)
//    util.solve(::partTwo)
}

data class Cell(val x: Long, val y: Long, val z: Long)

typealias Reactor = MutableSet<Cell>

data class Cuboid(val x: LongRange, val y: LongRange, val z: LongRange)

sealed class Step(val cuboid: Cuboid) {
    abstract fun execute(reactor: Reactor, constraint: LongRange)
}

class On(cuboid: Cuboid) : Step(cuboid) {
    override fun execute(reactor: Reactor, constraint: LongRange) {
        for (x in cuboid.x) {
            if (x !in constraint) continue
            for (y in cuboid.y) {
                if (y !in constraint) continue
                for (z in cuboid.z) {
                    if (z !in constraint) continue
                    reactor.add(Cell(x, y, z))
                }
            }
        }
    }
}

class Off(cuboid: Cuboid) : Step(cuboid) {
    override fun execute(reactor: Reactor, constraint: LongRange) {
        for (x in cuboid.x) {
            if (x !in constraint) continue
            for (y in cuboid.y) {
                if (y !in constraint) continue
                for (z in cuboid.z) {
                    if (z !in constraint) continue
                    reactor.remove(Cell(x, y, z))
                }
            }
        }
    }
}

// on x=10..12,y=10..12,z=10..12
private fun String.toStep(): Step {
    val parts = split(' ')
    val (x, y, z) = parts[1]
        .split(',')
        .map { dim ->
            val (a, b) = dim.split('=')[1]
                .split("..")
                .map(String::toLong)
            a..b
        }
    val c = Cuboid(x, y, z)
    return if (parts[0] == "on") On(c) else Off(c)
}

fun partOne(input: String) =
    input
        .lines()
        .map(String::toStep)
        .fold(HashSet<Cell>()) { reactor, step ->
            step.execute(reactor, -50L..50)
            reactor
        }
        .size

fun partTwo(input: String) = input.trim().length
