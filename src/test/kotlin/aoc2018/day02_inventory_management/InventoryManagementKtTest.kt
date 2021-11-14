package aoc2018.day02_inventory_management

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import util.cleanInput

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
        """.cleanInput()
            )
        )
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(-1, partTwo("input"))
    }

}
