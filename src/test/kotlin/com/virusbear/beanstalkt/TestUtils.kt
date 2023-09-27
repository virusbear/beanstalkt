package com.virusbear.beanstalkt

import com.virusbear.beanstalkt.operation.Operation
import com.virusbear.beanstalkt.response.Response
import com.virusbear.beanstalkt.response.ResponseType
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.test.assertIs

internal suspend inline fun <reified T: Response> ResponseType.readResponse(params: List<String>, channel: ByteReadChannel): T {
    val response = read(params, channel)

    assertIs<T>(response)

    return response
}

internal suspend inline fun <reified T> Operation<T>.writePacketAsText(): String =
    BytePacketBuilder().apply { this@writePacketAsText.write(this@apply) }.build().readText()