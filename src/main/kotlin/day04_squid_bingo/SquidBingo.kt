package day04_squid_bingo

fun main() {
    util.solve(16716, ::partOne)
    util.solve(4880, ::partTwo)
}

private class Board(numbers: List<Int>) {

    private val board: IntArray

    init {
        assert(numbers.size == 25)
        board = numbers.toIntArray()
    }

    fun markDrawn(drawn: Int) {
        val idx = board.indexOf(drawn)
        if (idx < 0) return
        board[idx] = -1
    }

    fun hasWon(): Boolean {
        for (r in 0 until 5) {
            if (((r * 5) until (r * 5 + 5)).all { board[it] < 0 })
                return true
        }
        for (c in 0 until 5) {
            if ((c until 25 step 5).all { board[it] < 0 })
                return true
        }
        return false
    }

    fun sumOfUnmarked() =
        board
            .asSequence()
            .filter { it >= 0 }
            .sum()
}

private val RE_SPACES = " +".toRegex()

private fun String.toBallsAndBoards(): Pair<List<Int>, List<Board>> {
    val lines = lines()
    val balls = lines
        .first()
        .split(',')
        .map(String::toInt)
    val boards = lines
        .drop(1)
        .chunked(6)
        .map { board ->
            board
                .flatMap { line ->
                    line
                        .split(RE_SPACES)
                        .filter(String::isNotBlank)
                        .map(String::toInt)
                }
        }
        .map(::Board)
    return Pair(balls, boards)
}

fun partOne(input: String): Int {
    val (balls, boards) = input.toBallsAndBoards()
    balls.forEach { ball ->
        boards.forEach { board ->
            board.markDrawn(ball)
            if (board.hasWon()) {
                return ball * board.sumOfUnmarked()
            }
        }
    }
    throw IllegalStateException("No winning board found?!")
}

fun partTwo(input: String): Int {
    val (balls, allBoards) = input.toBallsAndBoards()
    balls.fold(allBoards) { boards, ball ->
        boards.forEach {
            it.markDrawn(ball)
        }
        if (boards.size == 1 && boards.first().hasWon()) {
            return ball * boards.first().sumOfUnmarked()
        }
        boards.filter { !it.hasWon() }
    }
    throw IllegalStateException("No winning board found?!")
}
