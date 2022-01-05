package day19_beacon_scanner

import geom.Vec

class Scanner(val id: Int) {

    var beacons: List<Vec> = listOf()
    val beaconCount get() = beacons.size
    private var twists: Int = 0
    var location: Vec? = null

    fun add(beacon: Vec) {
        beacons = beacons + beacon
    }

    fun twist() {
        twists += 1
        val twisted = beacons.map { b ->
            // rotate around x
            var n = Vec(b.x, b.z, -b.y)
            if (twists % 4 == 0) {
                // invert x
                n = Vec(-n.x, n.y, n.z)
            }
            if (twists % 8 == 0) {
                // twist to next axis
                n = Vec(n.y, n.z, n.x)
            }
            if (twists % 24 == 0) {
                // reorder
                n = Vec(n.x, n.z, n.y)
            }
            n
        }
        beacons = twisted
    }

    override fun toString(): String =
        "Scanner #$id (${beaconCount} beacons)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Scanner

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}
