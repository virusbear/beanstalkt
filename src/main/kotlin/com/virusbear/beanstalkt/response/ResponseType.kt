package com.virusbear.beanstalkt.response

import io.ktor.utils.io.*

interface ResponseType {
    val code: String
    suspend fun read(params: List<String>, channel: ByteReadChannel): Response

    companion object {
        val All: Set<ResponseType> =
            setOf(
                OutOfMemoryResponse,
                InternalErrorResponse,
                BadFormatResponse,
                UnknownCommandResponse,
                ExpectedCrlfResponse,
                JobTooBigResponse,
                DrainingResponse,
                InsertedResponse,
                BuriedResponse,
                UsingResponse,
                ReservedResponse,
                DeadlineSoonResponse,
                TimedOutResponse,
                NotFoundResponse,
                ReleasedResponse,
                DeletedResponse,
                TouchedResponse,
                WatchingResponse,
                NotIgnoredResponse,
                FoundResponse,
                KickedResponse,
                OkResponse,
                PausedResponse
            )
    }
}