package com.virusbear.beanstalkt.response

import io.ktor.utils.io.*

internal class UsingResponse(
    val tube: String
): Response {
    companion object: ResponseType {
        override val code: String = "USING"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response =
            UsingResponse(params[0])
    }
}