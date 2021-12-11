package day10_syntax_scoring

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    [({(<(())[]>[[{[]{<()<>>
    [(()[<>])]({[<{<<[]>>(
    {([(<{}[<>[]}>{[]{[(<()>
    (((({<>}<{<{<>}{[]{[]{}
    [[<[([]))<([[{}[[()]]]
    [{[{({}]{}}([{[{{{}}([]
    {<[[]]>}<{[{[{[]{()[[[]
    [<(<(<(<{}))><([]([]()
    <{([([[(<>()){}]>(<<{{
    <{([{{}}[<[[[<>{}]]]>[]]
""".trimIndent()

internal class SyntaxScoringKtTest {

    @Test
    fun partOne() {
        assertEquals(26397, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(288957, partTwo(WORKED_EXAMPLE))
    }

}
