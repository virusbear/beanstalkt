package com.virusbear.beanstalkt.response

import com.virusbear.beanstalkt.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ReservedResponseTest {
    @Test
    fun `code is RESERVED`() {
        assertEquals("RESERVED", ReservedResponse.code)
    }

    @Test
    fun read() = runBlocking {
        val data = "Hello World!"
        val channel = ByteReadChannel(data + "\r\n")
        val size = data.toByteArray().size
        val jobId = 3210092833u
        val params = listOf("$jobId", "$size")

        val response = ReservedResponse.readResponse<ReservedResponse>(params, channel)

        assertEquals(jobId, response.job.id)

        assertEquals(size, response.job.data.remaining())
        val rawData = ByteArray(size)
        response.job.data.get(rawData)
        assertEquals(data, String(rawData))
    }
}