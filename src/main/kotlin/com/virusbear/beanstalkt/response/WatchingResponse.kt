package com.virusbear.beanstalkt.response

import io.ktor.utils.io.*

internal data class WatchingResponse(
    val count: UInt
): Response {
    companion object: ResponseType {
        override val code: String = "WATCHING"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response =
            WatchingResponse(params[0].toUInt())
    }
}