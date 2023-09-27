package com.virusbear.beanstalkt.response

import com.virusbear.beanstalkt.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class InsertedResponseTest {
    @Test
    fun `code is INSERTED`() {
        assertEquals("INSERTED", InsertedResponse.code)
    }

    @Test
    fun read() = runBlocking {
        val jobId = 4082194950u
        val params = listOf("$jobId")
        val channel = ByteReadChannel(byteArrayOf())
        val response = InsertedResponse.readResponse<InsertedResponse>(params, channel)

        assertEquals(jobId, response.id)
    }
}