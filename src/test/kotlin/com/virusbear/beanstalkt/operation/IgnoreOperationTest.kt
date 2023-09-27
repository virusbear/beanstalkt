package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotIgnoredException
import com.virusbear.beanstalkt.response.NotIgnoredResponse
import com.virusbear.beanstalkt.response.WatchingResponse
import com.virusbear.beanstalkt.writePacketAsText
import kotlinx.coroutines.runBlocking
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.Test

class IgnoreOperationTest {
    @Test
    fun write() = runBlocking {
        val tube = "default"

        val op = IgnoreOperation(tube)

        val packet = op.writePacketAsText()

        assertEquals("ignore default\r\n", packet)
    }

    @Test
    fun readWatchingResponse(): Unit = runBlocking {
        val tube = "default"

        val op = IgnoreOperation(tube)

        val watchingCount = 5u
        op.read(WatchingResponse(watchingCount))
        val count = op.await()

        assertEquals(watchingCount, count)
    }

    @Test
    fun readNotIgnoredResponse(): Unit = runBlocking {
        val tube = "default"

        val op = IgnoreOperation(tube)

        val exception = op.readResponse(NotIgnoredResponse).exceptionOrNull()
        assertNotNull(exception)
        assertIs<NotIgnoredException>(exception)
        assertEquals(tube, exception.tube)
    }
}