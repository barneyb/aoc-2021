package aoc2018.day02_inventory_management

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class InventoryManagementKtTest {

    @Test
    fun partOne() {
        assertEquals(
            12, partOne(
                """
                    abcdef
                    bababc
                    abbcde
                    abcccd
                    aabcdd
                    abcdee
                    ababab
                """.trimIndent()
            )
        )
    }

    @Test
    fun partTwo() {
        assertEquals(
            "fgij", partTwo(
                """
                    abcde
                    fghij
                    klmno
                    pqrst
                    fguij
                    axcye
                    wvxyz
                """.trimIndent()
            )
        )
    }

}
