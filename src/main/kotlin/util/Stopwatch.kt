package util

class Stopwatch(private val startedAt: Long = System.currentTimeMillis()) {

    val elapsed: Long
        get() = System.currentTimeMillis() - startedAt

}
