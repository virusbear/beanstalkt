package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.BuriedResponse
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class BuryOperationTest {
    @Test
    fun write() = runBlocking {
        val id = 712946673u
        val priority = 2870041978u

        val op = BuryOperation(id, priority)

        val packet = op.writePacketAsText()

        assertEquals("bury $id $priority\r\n", packet)
    }

    @Test
    fun read(): Unit = runBlocking {
        val id = 712946673u
        val priority = 2870041978u

        val op = BuryOperation(id, priority)

        op.read(BuriedResponse(id))
        op.await()

        val exception = op.readResponse(NotFoundResponse).exceptionOrNull()
        assertNotNull(exception)
        assertIs<NotFoundException>(exception)
    }
}