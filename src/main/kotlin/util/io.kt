package util

import com.github.ajalt.mordant.rendering.TextAlign
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import java.io.File
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

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
    5 -> "Five"
    6 -> "Six"
    7 -> "Seven"
    8 -> "Eight"
    9 -> "Nine"
    10 -> "Ten"
    else -> answerCount.toString()
}

fun <T : Any> solve(expected: T, solver: (String) -> T) {
    val input = getInput(solver.javaClass)
    val (actual: T, elapsed: Duration) = solveAndTime(solver, input)
    val correct = expected == actual
    val style = if (correct) TextColors.brightGreen else TextColors.red
    answer(actual, elapsed, style)
    if (!correct) throw AssertionError("expected '$expected', but got '$actual'")
}

private val CUTOFF_NO_REPEAT = 400_000_000.nanoseconds

private val CUTOFF_REPEAT_ONCE = 200_000_000.nanoseconds

private val CUTOFF_REPEAT_TWICE = 100_000_000.nanoseconds

private val CUTOFF_REPEAT_THRICE = 50_000_000.nanoseconds

private fun <T : Any> solveAndTime(
    solver: (String) -> T,
    input: String
): Pair<T, Duration> {
    var actual: T
    var elapsed: Duration
    var repeat = 0
    while (true) {
        val watch = Stopwatch()
        actual = solver.invoke(input)
        elapsed = watch.elapsed
        if (repeat > 0 && elapsed > CUTOFF_NO_REPEAT) break
        if (repeat > 1 && elapsed > CUTOFF_REPEAT_ONCE) break
        if (repeat > 2 && elapsed > CUTOFF_REPEAT_TWICE) break
        if (repeat > 3 && elapsed > CUTOFF_REPEAT_THRICE) break
        if (repeat > 4) break
        ++repeat
    }
    return Pair(actual, elapsed)
}

fun solve(solver: (String) -> Any) {
    val input = getInput(solver.javaClass)
    val (actual: Any, elapsed: Duration) = solveAndTime(solver, input)
    answer(actual, elapsed)
}

fun answer(
    ans: Any,
    elapsed: Duration,
    style: TextStyle = TextColors.brightBlue
) {
    Terminal().println(table {
        borderTextStyle = style
        body {
            row(
                style("Answer ${nextAnswerLabel()}") + " " +
                        TextColors.gray("($elapsed)") + ": " +
                        TextStyles.bold(ans.toString())
            )
        }
    })
}

private val Duration.nanoseconds
    get() = toDouble(DurationUnit.NANOSECONDS)

private val CUTOFF_BENCHMARK_DURATION = 2_000_000_000.nanoseconds

private const val CUTOFF_BENCHMARK_ITERATIONS = 2000

fun <T : Any> benchmark(expected: T, solver: (String) -> T) {
    val input = getInput(solver.javaClass)
    fun check(actual: T) {
        if (expected != actual)
            throw AssertionError("expected '$expected', but got '$actual'")
    }
    print("benchmarking...")
    repeat(5) {
        check(solver(input))
    }
    print("warmed up...")
    val samples = mutableListOf<Duration>()
    var total = Duration.ZERO
    while (total < CUTOFF_BENCHMARK_DURATION && samples.size < CUTOFF_BENCHMARK_ITERATIONS) {
        val watch = Stopwatch()
        val actual = solver(input)
        val elapsed = watch.elapsed
        check(actual)
        total += elapsed
        samples.add(elapsed)
    }
    println("done!")
    val N = samples.size
    val mean = total.nanoseconds / N
    val variance =
        samples.sumOf { (it.nanoseconds - mean).pow(2.0) } / N
    val stddev = sqrt(variance)
    val ci95 = 1.96 * stddev / sqrt(N.toDouble())
    val durationString = "" + mean.nanoseconds + " ± " + ci95.nanoseconds
    // since correctness was checked each iteration, all is well at this point
    Terminal().println(table {
        borderTextStyle = TextColors.brightGreen
        body {
            row(
                TextColors.brightGreen("Benchmark ${nextAnswerLabel()}") + " " +
                        TextColors.gray("($durationString, N=$N)") + ": " +
                        TextStyles.bold(expected.toString())
            )
        }
    })

    barChart(
        continuousHistogram(
            samples,
            keySelector = Duration::inWholeNanoseconds
        ),
        labelSelector = { it.nanoseconds.toString() }
    )
}

private val ZERO_GRAY = TextColors.gray(fraction = 0.667)

fun <V> barChart(
    data: Map<V, Int>,
    labelSelector: (V) -> String = { it.toString() },
) {
    val maxVal = data.values.maxOf { it }
    Terminal().println(table {
        borderTextStyle = TextColors.gray
        borders = Borders.LEFT_RIGHT
        column(2) {
            align = TextAlign.RIGHT
        }
        body {
            data.keys.forEach { b ->
                val n = data.getOrDefault(b, 0)
                val bar = "█".repeat(ceil(n * 50.0 / maxVal).toInt())
                row(
                    labelSelector(b).let {
                        if (n == 0)
                            ZERO_GRAY(it)
                        else
                            it
                    },
                    TextColors.brightBlue(bar),
                    if (n == 0)
                        ""
                    else
                        n.toString()
                )
            }
        }
    })
}
