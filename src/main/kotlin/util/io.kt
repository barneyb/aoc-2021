package util

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import java.io.File

fun getInput() = File("input.txt")
    .canonicalFile
    .readText()
    .trimEnd('\n')

fun answer(ans: Any) {
    inBox(
        TextColors.brightGreen("Answer " + nextAnswerLabel()) + ": " +
                TextStyles.bold(ans.toString())
    )
}

fun answer(ans: Any, elapsed: Long) {
    inBox(
        TextColors.brightGreen("Answer " + nextAnswerLabel()) + " " +
                TextColors.gray("(%,d ms)".format(elapsed)) + ": " +
                TextStyles.bold(ans.toString())
    )
}

private fun inBox(text: String) {
    Terminal().println(table {
        borderTextStyle = TextColors.brightGreen
        body { row(text) }
    })
}

private var answerCount = 0
private fun nextAnswerLabel() = when (++answerCount) {
    1 -> "One"
    2 -> "Two"
    3 -> "Three"
    4 -> "Four"
    else -> answerCount.toString()
}

fun solve(solver: (String) -> Any) {
    val input = getInput()
    val watch = Stopwatch()
    answer(solver.invoke(input), watch.elapsed)
}
