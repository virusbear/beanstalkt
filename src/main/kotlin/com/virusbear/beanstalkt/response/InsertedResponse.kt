package com.virusbear.beanstalkt.response

import io.ktor.utils.io.*

internal data class InsertedResponse(
    val id: UInt
): Response {
    companion object: ResponseType {
        override val code: String = "INSERTED"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response =
            InsertedResponse(params.first().toUInt())
    }
}