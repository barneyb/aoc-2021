package util

import org.junit.jupiter.api.Test

internal class HistogramKtTest {

    @Test
    fun letterFreq() {
        val hist = discreteHistogram(
            "kjhdfkghdfkjghdfkjhdfkg".toCharArray().toList()
        ) { it }.makeComplete('d'..'k')
        barChart(hist.toSortedMap())
    }

}
