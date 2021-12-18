package day16_packet_decoder

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class PacketDecoderKtTest {

    @Test
    fun bitSeq() {
        val itr = "D2".asHexBitSequence()
        assertTrue(itr.next())
        assertTrue(itr.next())
        assertFalse(itr.next())
        assertTrue(itr.next())

        assertFalse(itr.next())
        assertFalse(itr.next())
        assertTrue(itr.next())
        assertFalse(itr.next())

        assertFalse(itr.hasNext())
    }

    @Test
    fun readInt() {
        val itr = "D2".asHexBitSequence()
        assertEquals(6, itr.readInt(3))
        assertEquals(4, itr.readInt(3))
    }

    @Test
    fun parseLiteral() {
        assertEquals(Literal(6, 4, 2021), "D2FE28".toPacket())
    }

    @Test
    fun parseBitLenOp() {
        assertEquals(
            Operator(
                1, 6, listOf(
                    Literal(6, 4, 10),
                    Literal(2, 4, 20),
                )
            ), "38006F45291200".toPacket()
        )
    }

    @Test
    fun parsePacketLenOp() {
        assertEquals(
            Operator(
                7, 3, listOf(
                    Literal(2, 4, 1),
                    Literal(4, 4, 2),
                    Literal(1, 4, 3),
                )
            ), "EE00D40C823060".toPacket()
        )
    }

    @Test
    fun partOne() {
        assertEquals(16, partOne("8A004A801A8002F478"))
        assertEquals(12, partOne("620080001611562C8802118E34"))
        assertEquals(23, partOne("C0015000016115A2E0802F182340"))
        assertEquals(31, partOne("A0016C880162017C3686B18A3D4780"))
    }

}
