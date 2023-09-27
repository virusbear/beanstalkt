package com.virusbear.beanstalkt.response

import com.virusbear.beanstalkt.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class FoundResponseTest {
    @Test
    fun `code is FOUND`() {
        assertEquals("FOUND", FoundResponse.code)
    }

    @Test
    fun read() = runBlocking {
        val data = "Hello World!"
        val channel = ByteReadChannel(data + "\r\n")
        val size = data.toByteArray().size
        val jobId = 1381726509u
        val params = listOf("$jobId", "$size")

        val response = FoundResponse.readResponse<FoundResponse>(params, channel)

        assertEquals(jobId, response.job.id)

        assertEquals(size, response.job.data.remaining())
        val rawData = ByteArray(size)
        response.job.data.get(rawData)
        assertEquals(data, String(rawData))
    }
}