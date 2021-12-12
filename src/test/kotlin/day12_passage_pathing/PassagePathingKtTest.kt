package day12_passage_pathing

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val EXAMPLE_SMALL = """
    start-A
    start-b
    A-c
    A-b
    b-d
    A-end
    b-end
""".trimIndent()

private val EXAMPLE_MEDIUM = """
    dc-end
    HN-start
    start-kj
    dc-start
    dc-HN
    LN-dc
    HN-end
    kj-sa
    kj-HN
    kj-dc
""".trimIndent()

private val EXAMPLE_LARGE = """
    fs-end
    he-DX
    fs-he
    start-DX
    pj-DX
    end-zg
    zg-sl
    zg-pj
    pj-he
    RW-he
    fs-DX
    pj-RW
    zg-RW
    start-pj
    he-WI
    zg-he
    pj-fs
    start-RW
""".trimIndent()

internal class PassagePathingKtTest {

    @Test
    fun partOne_small() {
        assertEquals(10, partOne(EXAMPLE_SMALL))
    }

    @Test
    fun partOne_medium() {
        assertEquals(19, partOne(EXAMPLE_MEDIUM))
    }

    @Test
    fun partOne_large() {
        assertEquals(226, partOne(EXAMPLE_LARGE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(EXAMPLE_SMALL))
    }

}
