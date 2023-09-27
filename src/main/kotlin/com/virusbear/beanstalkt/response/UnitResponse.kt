package com.virusbear.beanstalkt.response

import io.ktor.utils.io.*

internal abstract class UnitResponse(
    override val code: String
): Response, ResponseType {
    override suspend fun read(params: List<String>, channel: ByteReadChannel): Response =
        this
}