package com.virusbear.beanstalkt.response

import com.virusbear.beanstalkt.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class OkResponseTest {
    @Test
    fun `code is OK`() {
        assertEquals("OK", OkResponse.code)
    }

    @Test
    fun `read yaml list`() = runBlocking {
        val yaml = """
            ---
            - value1
            - "value with special - character"
            - value with whitespace
            ---""".trimIndent()
        val requestRaw = yaml.toByteArray()

        val params = listOf("${requestRaw.size}")
        val channel = ByteReadChannel(yaml + "\r\n")
        val response = OkResponse.readResponse<OkResponse>(params, channel)
        assertContentEquals(listOf("value1", "value with special - character", "value with whitespace"), response.stats.keys)
    }

    @Test
    fun `read yaml map`() = runBlocking {
        val yaml = """
            ---
            key1: value1
            key_2: value with whitespaces
            "key3": "value with special - character"
            ---""".trimIndent()
        val requestRaw = yaml.toByteArray()

        val params = listOf("${requestRaw.size}")
        val channel = ByteReadChannel(yaml + "\r\n")
        val response = OkResponse.readResponse<OkResponse>(params, channel)

        val expectedContent = mapOf(
            "key1" to "value1",
            "key_2" to "value with whitespaces",
            "key3" to "value with special - character"
        )

        assertContentEquals(expectedContent.keys.toList(), response.stats.keys.toList())
        assertContentEquals(expectedContent.values, response.stats.values)
    }
}