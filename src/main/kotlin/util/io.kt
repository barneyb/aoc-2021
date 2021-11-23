package util

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import java.io.File
import java.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration

/**
 * It's unlikely you want this one; use [getInput] passing "self class".
 */
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
    val input = getInput(solver.javaClass)
    val (actual: T, elapsed: Long) = solveAndTime(solver, input)
    val correct = expected == actual
    val style = if (correct) TextColors.brightGreen else TextColors.red
    answer(actual, elapsed, style)
    if (!correct) throw AssertionError("expected '$expected', but got '$actual'")
}

private fun <T : Any> solveAndTime(
    solver: (String) -> T,
    input: String
): Pair<T, Long> {
    var actual: T
    var elapsed: Long
    var repeat = 0
    while (true) {
        val watch = Stopwatch()
        actual = solver.invoke(input)
        elapsed = watch.elapsed
        if (repeat > 0 || elapsed > 500) break
        if (repeat > 1 || elapsed > 200) break
        if (repeat > 2 || elapsed > 100) break
        if (repeat > 3 || elapsed > 50) break
        ++repeat
    }
    return Pair(actual, elapsed)
}

fun solve(solver: (String) -> Any) {
    val input = getInput(solver.javaClass)
    val (actual: Any, elapsed: Long) = solveAndTime(solver, input)
    answer(actual, elapsed)
}

@OptIn(ExperimentalTime::class)
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
                        TextColors.gray(
                            "(" + Duration.ofMillis(elapsed).toKotlinDuration()
                                .toString()
                        ) + "): " +
                        TextStyles.bold(ans.toString())
            )
        }
    })
}
