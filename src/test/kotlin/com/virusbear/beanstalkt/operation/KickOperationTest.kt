package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.KickedResponse
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.*

class KickOperationTest {
    @Test
    fun `no parameters throws exception`(): Unit = runBlocking {
        val op = KickOperation()

        assertThrows<IllegalStateException> {
            op.writePacketAsText()
        }
    }

    @Test
    fun `write with bound`() = runBlocking {
        val bound = 831836u
        val op = KickOperation(bound = bound)

        val packet = op.writePacketAsText()

        assertEquals("kick $bound\r\n", packet)
    }

    @Test
    fun `write with id`() = runBlocking {
        val id = 638287u
        val op = KickOperation(id = id)

        val packet = op.writePacketAsText()

        assertEquals("kick-job $id\r\n", packet)
    }

    @Test
    fun `write with bound and id uses bound`() = runBlocking {
        val id = 962372u
        val bound = 247533u
        val op = KickOperation(bound = bound, id = id)

        val packet = op.writePacketAsText()

        assertEquals("kick $bound\r\n", packet)
    }
    
    @Test
    fun `read kicked response`(): Unit = runBlocking {
        val responseCount = 803286u
        val op = KickOperation()

        op.read(KickedResponse(responseCount))
        val count = op.await()

        assertEquals(responseCount, count)
    }

    @Test
    fun `read not found response`(): Unit = runBlocking {
        val op = KickOperation()

        val exception = op.readResponse(NotFoundResponse).exceptionOrNull()
        assertNotNull(exception)
        assertIs<NotFoundException>(exception)
    }
}