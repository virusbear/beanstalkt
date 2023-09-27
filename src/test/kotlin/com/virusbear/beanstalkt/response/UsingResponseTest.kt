package com.virusbear.beanstalkt.response

import com.virusbear.beanstalkt.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UsingResponseTest {
    @Test
    fun `code is USING`() {
        assertEquals("USING", UsingResponse.code)
    }

    @Test
    fun read() = runBlocking {
        val tube = "default"
        val params = listOf(tube)
        val channel = ByteReadChannel(byteArrayOf())

        val response = UsingResponse.readResponse<UsingResponse>(params, channel)

        assertEquals(tube, response.tube)
    }
}