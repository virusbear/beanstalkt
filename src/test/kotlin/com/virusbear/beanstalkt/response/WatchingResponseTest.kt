package com.virusbear.beanstalkt.response

import com.virusbear.beanstalkt.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchingResponseTest {
    @Test
    fun `code is WATCHING`() {
        assertEquals("WATCHING", WatchingResponse.code)
    }

    @Test
    fun read() = runBlocking {
        val count = 263778768u
        val params = listOf("$count")
        val channel = ByteReadChannel(byteArrayOf())

        val response = WatchingResponse.readResponse<WatchingResponse>(params, channel)

        assertEquals(count, response.count)
    }
}