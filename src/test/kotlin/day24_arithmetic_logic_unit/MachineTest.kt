package day24_arithmetic_logic_unit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MachineTest {

    @Test
    fun invert() {
        val program = """
            inp x
            mul x -1
        """.trimIndent()
        var m = Machine()
        m.execute(program, listOf(7))
        assertEquals(-7, m.x)

        m = Machine()
        m.execute(program, listOf(-3))
        assertEquals(3, m.x)
    }

    @Test
    fun isTriple() {
        val program = """
            inp z
            inp x
            mul z 3
            eql z x
        """.trimIndent()
        var m = Machine()
        m.execute(program, listOf(2, 6))
        assertEquals(1, m.z)

        m = Machine()
        m.execute(program, listOf(2, -6))
        assertEquals(0, m.z)
    }

    @Test
    fun toBinary() {
        val program = """
            inp w
            add z w
            mod z 2
            div w 2
            add y w
            mod y 2
            div w 2
            add x w
            mod x 2
            div w 2
            mod w 2
        """.trimIndent()
        var m = Machine()
        m.execute(program, listOf(64))
        assertEquals(0, m.w)
        assertEquals(0, m.x)
        assertEquals(0, m.y)
        assertEquals(0, m.z)

        m = Machine()
        m.execute(program, listOf(4))
        assertEquals(0, m.w)
        assertEquals(1, m.x)
        assertEquals(0, m.y)
        assertEquals(0, m.z)
    }

}
