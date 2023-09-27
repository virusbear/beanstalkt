package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.DeletedResponse
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class DeleteOperationTest {
    @Test
    fun write() = runBlocking {
        val id = 3397947682u

        val op = DeleteOperation(id)

        val packet = op.writePacketAsText()

        assertEquals("delete $id\r\n", packet)
    }

    @Test
    fun read(): Unit = runBlocking {
        val id = 2415865491u

        val op = DeleteOperation(id)

        op.read(DeletedResponse)
        op.await()

        val exception = op.readResponse(NotFoundResponse).exceptionOrNull()
        assertNotNull(exception)
        assertIs<NotFoundException>(exception)
    }
}