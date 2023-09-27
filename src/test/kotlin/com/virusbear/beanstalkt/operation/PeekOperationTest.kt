package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.JobState
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class PeekOperationTest {
    @Test
    fun `no parameters throws exception`(): Unit = runBlocking {
        val op = PeekOperation()

        assertThrows<IllegalStateException> {
            op.writePacketAsText()
        }
    }

    @Test
    fun `write with id`() = runBlocking {
        val id = 631252u
        val op = PeekOperation(id = id)

        val packet = op.writePacketAsText()

        assertEquals("peek $id\r\n", packet)
    }

    @Test
    fun `write peek ready`() = runBlocking {
        val op = PeekOperation(state = JobState.Ready)

        val packet = op.writePacketAsText()

        assertEquals("peek-ready\r\n", packet)
    }

    @Test
    fun `write peek delayed`() = runBlocking {
        val op = PeekOperation(state = JobState.Delayed)

        val packet = op.writePacketAsText()

        assertEquals("peek-delayed\r\n", packet)
    }

    @Test
    fun `write peek buried`() = runBlocking {
        val op = PeekOperation(state = JobState.Buried)

        val packet = op.writePacketAsText()

        assertEquals("peek-buried\r\n", packet)
    }

    @Test
    fun `write peek reserved throws exception`(): Unit = runBlocking {
        val op = PeekOperation(state = JobState.Reserved)

        assertThrows<IllegalStateException> {
            op.writePacketAsText()
        }
    }
}
