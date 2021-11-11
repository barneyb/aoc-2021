package util

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import java.io.File

fun getInput(): String {
    val ctxFile = File("input.txt")
    if (ctxFile.exists()) {
        return ctxFile
            .readText()
            .cleanInput()
    }
    return getInput(
        Class.forName("sun.launcher.LauncherHelper")
            .getMethod("getApplicationClass")
            .invoke(null) as Class<*>
    )
}

fun getInput(clazz: Class<*>): String {
    return clazz.classLoader
        .getResource(clazz.packageName.replace('.', '/') + "/input.txt")!!
        .readText()
        .cleanInput()
}

fun String.cleanInput() =
    this.trim('\n')

private var answerCount = 0
private fun nextAnswerLabel() = when (++answerCount) {
    1 -> "One"
    2 -> "Two"
    3 -> "Three"
    4 -> "Four"
    else -> answerCount.toString()
}

fun <T : Any> solve(expected: T, solver: (String) -> T) {
    val input = getInput()
    val watch = Stopwatch()
    val actual = solver.invoke(input)
    val elapsed = watch.elapsed
    val correct = expected == actual
    val style = if (correct)
        TextColors.brightGreen
    else
        TextColors.red
    answer(actual, elapsed, style)
    if (!correct) throw AssertionError("expected '$expected', but got '$actual'")
}

fun solve(solver: (String) -> Any) {
    val input = getInput()
    val watch = Stopwatch()
    answer(solver.invoke(input), watch.elapsed)
}

fun answer(
    ans: Any,
    elapsed: Long,
    style: TextStyle = TextColors.brightBlue
) {
    Terminal().println(table {
        borderTextStyle = style
        body {
            row(
                style("Answer " + nextAnswerLabel()) + " " +
                        TextColors.gray("(%,d ms)".format(elapsed)) + ": " +
                        TextStyles.bold(ans.toString())
            )
        }
    })
}
