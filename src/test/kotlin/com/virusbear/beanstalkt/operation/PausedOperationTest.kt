package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.*
import com.virusbear.beanstalkt.response.*
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import kotlin.test.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class PausedOperationTest {
    @Test
    fun write() = runBlocking {
        val tube = "default"
        val delay = 10.seconds
        val op = PausedOperation(tube, delay)

        val packet = op.writePacketAsText()

        assertEquals("pause-tube $tube ${delay.inWholeSeconds}\r\n", packet)
    }

    @Test
    fun `read paused response`(): Unit = runBlocking {
        val op = PausedOperation("", Duration.ZERO)

        val exception = op.readResponse(PausedResponse).exceptionOrNull()

        assertNull(exception)
    }

    @Test
    fun `read not found response`(): Unit = runBlocking {
        val op = PausedOperation("", Duration.ZERO)

        val exception = op.readResponse(NotFoundResponse).exceptionOrNull()

        assertNotNull(exception)
        assertIs<NotFoundException>(exception)
    }
}
