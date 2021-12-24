package day22_reactor_reboot

import geom.Cuboid

data class Surgery(
    val fromOne: List<Cuboid>,
    val fromTwo: List<Cuboid>,
    val fromBoth: List<Cuboid>
) {

    val isNoop = fromBoth.isEmpty()

    val all
        get(): List<Cuboid> = mutableListOf<Cuboid>()
            .apply {
                addAll(fromOne)
                addAll(fromTwo)
                addAll(fromBoth)
            }
}

fun performSurgery(one: Cuboid, two: Cuboid): Surgery {
    val intersect = one.intersection(two)
    if (intersect.isEmpty()) {
        return Surgery(listOf(one), listOf(two), emptyList())
    }
    val fromOne = mutableListOf<Cuboid>()
    val fromTwo = mutableListOf<Cuboid>()
    val oneDims = one.dims.copyOf()
    val twoDims = two.dims.copyOf()
    for ((idx, i) in intersect.dims.withIndex()) {
        fun doIt(dims: Array<LongRange>, from: MutableList<Cuboid>) {
            val c = dims[idx]
            if (c.first < i.first) {
                dims[idx] = c.first until i.first
                from.add(Cuboid(dims.copyOf()))
            }
            if (c.last > i.last) {
                dims[idx] = (i.last + 1)..c.last
                from.add(Cuboid(dims.copyOf()))
            }
            dims[idx] = i
        }
        doIt(oneDims, fromOne)
        doIt(twoDims, fromTwo)
    }
    return Surgery(fromOne, fromTwo, listOf(intersect))
}
