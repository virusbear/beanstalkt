package com.virusbear.beanstalkt.response

import io.ktor.utils.io.*

internal data class BuriedResponse(
    val id: UInt? = null
): Response {
    companion object: ResponseType {
        override val code: String = "BURIED"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response =
            BuriedResponse(params.firstOrNull()?.toUInt())
    }
}