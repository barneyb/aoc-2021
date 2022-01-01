package day23_amphipod

enum class Cell(
    var symbol: Char,
    val stepEnergy: Long = 0,
    val homeX: Long = -1
) {
    WALL('#'),
    OPEN('.'),
    AMBER('A', 1, 3),
    BRONZE('B', 10, 5),
    COPPER('C', 100, 7),
    DESERT('D', 1000, 9);

    fun isAmphipod() = stepEnergy != 0L
}
