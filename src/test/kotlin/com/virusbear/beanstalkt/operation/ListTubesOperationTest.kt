package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.response.OkResponse
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ListTubesOperationTest {
    @Test
    fun `write list all tubes`(): Unit = runBlocking {
        val op = ListTubesOperation()

        val packet = op.writePacketAsText()

        assertEquals("list-tubes\r\n", packet)
    }

    @Test
    fun `write list watched tubes`(): Unit = runBlocking {
        val op = ListTubesOperation(watched = true)

        val packet = op.writePacketAsText()

        assertEquals("list-tubes-watched\r\n", packet)
    }

    @Test
    fun read(): Unit = runBlocking {
        val op = ListTubesOperation()

        val responseTubes = mapOf(
            "default" to "",
            "test" to ""
        )

        op.read(OkResponse(responseTubes))
        val tubes = op.await()

        assertContentEquals(responseTubes.keys.toList(), tubes)
    }
}
