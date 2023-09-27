package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.BuriedException
import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.BuriedResponse
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.response.ReleasedResponse
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import kotlin.test.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ReleaseOperationTest {
    @Test
    fun write() = runBlocking {
        val id = 746979u
        val priority = 239219u
        val delay = 774906.0.seconds

        val op = ReleaseOperation(id, priority, delay)

        val packet = op.writePacketAsText()

        assertEquals("release $id $priority ${delay.inWholeSeconds}\r\n", packet)
    }

    @Test
    fun `read released response`() = runBlocking {
        val op = ReleaseOperation(0u, 0u, Duration.ZERO)

        assertTrue(op.readResponse(ReleasedResponse).isSuccess)
    }

    @Test
    fun `read buried response without id`() = runBlocking {
        val id = 379923u
        val op = ReleaseOperation(id, 0u, Duration.ZERO)

        val exception = op.readResponse(BuriedResponse()).exceptionOrNull()

        assertNotNull(exception)
        assertIs<BuriedException>(exception)

        assertEquals(id, exception.id)
    }

    @Test
    fun `read buried response with id`() = runBlocking {
        val id = 68053u
        val op = ReleaseOperation(0u, 0u, Duration.ZERO)

        val exception = op.readResponse(BuriedResponse(id)).exceptionOrNull()

        assertNotNull(exception)
        assertIs<BuriedException>(exception)

        assertEquals(id, exception.id)
    }

    @Test
    fun `read not found response`(): Unit = runBlocking {
        val op = ReleaseOperation(0u, 0u, Duration.ZERO)

        val exception = op.readResponse(NotFoundResponse).exceptionOrNull()

        assertNotNull(exception)
        assertIs<NotFoundException>(exception)
    }
}