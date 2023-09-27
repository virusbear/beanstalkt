package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.response.UsingResponse
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ListTubeUsedOperationTest {
    @Test
    fun write() = runBlocking {
        val op = ListTubeUsedOperation()

        val packet = op.writePacketAsText()

        assertEquals("list-tube-used\r\n", packet)
    }

    @Test
    fun read() = runBlocking {
        val tube = "default"
        val op = ListTubeUsedOperation()
        val response = UsingResponse(tube)

        op.read(response)
        val using = op.await()

        assertEquals(tube, using)
    }
}
