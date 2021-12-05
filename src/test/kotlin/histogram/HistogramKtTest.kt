package histogram

import org.junit.jupiter.api.Test
import util.barChart

internal class HistogramKtTest {

    @Test
    fun letterFreq() {
        val hist = histogramOf(
            "kjhdfkghdfkjghdfkjhdfkg".toCharArray().toList()
        ).makeComplete('d'..'k')
        barChart(hist.toSortedMap())
    }

}
