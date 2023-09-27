package com.virusbear.beanstalkt.response

import com.virusbear.beanstalkt.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class KickedResponseTest {
    @Test
    fun `code is KICKED`() {
        assertEquals("KICKED", KickedResponse.code)
    }

    @Test
    fun `count equals parameter`() = runBlocking {
        val count = 1513u
        val params = listOf("$count")
        val channel = ByteReadChannel(byteArrayOf())
        val response = KickedResponse.readResponse<KickedResponse>(params, channel)

        assertEquals(count, response.count)
    }

    @Test
    fun `count is 0 if not in params`() = runBlocking {
        val params = emptyList<String>()
        val channel = ByteReadChannel(byteArrayOf())
        val response = KickedResponse.readResponse<KickedResponse>(params, channel)

        assertEquals(0u, response.count)
    }
}