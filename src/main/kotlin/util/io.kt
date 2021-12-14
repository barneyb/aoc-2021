package util

import com.github.ajalt.mordant.rendering.TextAlign
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import histogram.continuousHistogramOf
import java.io.File
import java.io.OutputStream
import java.io.PrintWriter
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.KCallable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

internal fun saveFile(outFile: File, work: (OutputStream) -> Unit) {
    val out = outFile.outputStream()
    val watch = Stopwatch()
    work(out)
    val elapsed = watch.elapsed
    out.close() // don't include system/IO "stuff" in timing
    answer(outFile, elapsed, label = "Saved file")
}

internal fun outputFile(source: Any, extension: String = "txt") =
    File("files/${source.javaClass.packageName}.$extension")

fun saveTextFile(
    work: (String, PrintWriter) -> Unit,
    extension: String = "txt"
) =
    saveFile(outputFile(work, extension)) { out ->
        val pw = PrintWriter(out)
        work(getInput(work.javaClass), pw)
        pw.close()
    }

fun getInput(clazz: Class<*>) =
    getInput(clazz.packageName)

fun getInput(packageName: String): String =
    Thread
        .currentThread()
        .contextClassLoader
        .getResource(packageName.replace('.', '/') + "/input.txt")!!
        .readText()
        .cleanInput()

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
    val correct = if (actual is String && actual.contains('\n')) {
        (expected as String).trimIndent() == actual.trimIndent()
    } else {
        expected == actual
    }
    val style = if (correct) TextColors.brightGreen else TextColors.red
    answer(actual, elapsed, style, (solver as KCallable<*>).name)
    if (!correct) throw AssertionError("expected '$expected', but got '$actual'")
}

private fun <T : Any> solveAndTime(
    solver: (String) -> T,
    input: String
): Pair<T, Duration> {
    val watch = Stopwatch()
    val actual = solver.invoke(input)
    val elapsed = watch.elapsed
    return Pair(actual, elapsed)
}

fun solve(solver: (String) -> Any) {
    val input = getInput(solver.javaClass)
    val (actual: Any, elapsed: Duration) = solveAndTime(solver, input)
    answer(actual, elapsed, label = (solver as KCallable<*>).name)
}

fun answer(
    ans: Any,
    elapsed: Duration,
    style: TextStyle = TextColors.brightBlue,
    label: String = "Answer ${nextAnswerLabel()}",
) = printBoxed(
    style(label) + " " +
            TextColors.gray("(${elapsed.toPrettyString()})") + ": " +
            TextStyles.bold(ans.toString().let {
                if (it.contains('\n'))
                    "\n$it"
                else
                    it
            }),
    style
)

private val Duration.nanoseconds
    get() = toDouble(DurationUnit.NANOSECONDS)

private val CUTOFF_BENCHMARK_DURATION = 2_000_000_000.nanoseconds

private const val CUTOFF_BENCHMARK_ITERATIONS = 2000

@Suppress("unused")
fun <T : Any> benchmark(expected: T, solver: (String) -> T) {
    val (samples, total) = bench(expected, solver)
    benchSummary(expected, samples, total, (solver as KCallable<*>).name)
}

private fun <T : Any> bench(
    expected: T,
    solver: (String) -> T
): Pair<List<Duration>, Duration> {
    val input = getInput(solver.javaClass)
    fun check(actual: T) {
        if (expected != actual)
            throw AssertionError("expected '$expected', but got '$actual'")
    }
    print("benchmarking...")
    val warmupWatch = Stopwatch()
    var warmupCount = 0
    while (warmupCount++ < 5 && warmupWatch.elapsed < CUTOFF_BENCHMARK_DURATION) {
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
    return Pair(samples, total)
}

private fun <T : Any> benchSummary(
    expected: T,
    samples: Collection<Duration>,
    total: Duration,
    label: String = "Benchmark ${nextAnswerLabel()}",
) {
    val N = samples.size
    val mean = total.nanoseconds / N
    val variance =
        samples.sumOf { (it.nanoseconds - mean).pow(2.0) } / N
    val stddev = sqrt(variance)
    val ci95 = 1.96 * stddev / sqrt(N.toDouble())
    // since correctness was checked each iteration, all is well at this point
    printBoxed(
        TextColors.magenta(label) + TextColors.gray(
            " (" +
                    TextColors.black(TextStyles.bold(mean.nanoseconds.toPrettyString())) +
                    " ± ${ci95.nanoseconds.toPrettyString()}," +
                    " N=${TextColors.black(N.toString())}): " +
                    expected.toString()
        ), TextColors.magenta
    )
}

fun printBoxed(value: Any, borderTextStyle: TextStyle = TextColors.black) {
    Terminal().println(table {
        this.borderTextStyle = borderTextStyle
        body {
            row(value)
        }
    })
}

private fun Duration.toPrettyString(): String {
    val s = toString()
    if (s.endsWith("us")) {
        return s.substring(0, s.length - 2) + "μs"
    }
    return s
}

@Suppress("unused")
fun <T : Any> benchAndHist(
    expected: T,
    solver: (String) -> T,
    bucketCount: Int = 10
) {
    val (samples, total) = bench(expected, solver)
    benchSummary(expected, samples, total, (solver as KCallable<*>).name)

    barChart(
        continuousHistogramOf(
            samples,
            keySelector = Duration::inWholeNanoseconds,
            bucketCount = bucketCount,
        ),
        labelSelector = { it.nanoseconds.toPrettyString() }
    )
}

private val ZERO_GRAY = TextColors.gray(fraction = 0.667)

fun <V> barChart(
    data: Map<V, Long>,
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
                        if (n == 0L)
                            ZERO_GRAY(it)
                        else
                            it
                    },
                    TextColors.brightBlue(bar),
                    if (n == 0L)
                        ""
                    else
                        n.toString()
                )
            }
        }
    })
}
