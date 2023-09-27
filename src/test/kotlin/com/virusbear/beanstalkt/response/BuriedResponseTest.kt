package com.virusbear.beanstalkt.response

import com.virusbear.beanstalkt.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class BuriedResponseTest {
    @Test
    fun `code is BURIED`() {
        assertEquals("BURIED", BuriedResponse.code)
    }

    @Test
    fun readWithId() = runBlocking {
        val jobId = 975682734u
        val params = listOf("$jobId")
        val channel = ByteReadChannel(byteArrayOf())
        val response = BuriedResponse.readResponse<BuriedResponse>(params, channel)

        assertNotNull(response.id)
        assertEquals(jobId, response.id)
    }

    @Test
    fun readWithoutId() = runBlocking {
        val params = emptyList<String>()
        val channel = ByteReadChannel(byteArrayOf())
        val response = BuriedResponse.readResponse<BuriedResponse>(params, channel)

        assertNull(response.id)
    }
}