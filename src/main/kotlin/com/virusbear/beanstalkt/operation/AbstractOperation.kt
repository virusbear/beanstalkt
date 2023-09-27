package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.*
import com.virusbear.beanstalkt.response.*
import kotlinx.coroutines.CompletableDeferred

internal abstract class AbstractOperation<T> private constructor(
    private val result: CompletableDeferred<T>
): Operation<T>, CompletableDeferred<T> by result {
    constructor(): this(CompletableDeferred())

    open suspend fun readResponse(response: Response): Result<T> =
        when(response) {
            is OutOfMemoryResponse -> Result.failure(OutOfMemoryException())
            is InternalErrorResponse -> Result.failure(InternalErrorException())
            is BadFormatResponse -> Result.failure(BadFormatException())
            is UnknownCommandResponse -> Result.failure(UnknownCommandException())
            else -> Result.failure(RuntimeException("An unknown error occurred reading response of type ${response::class.simpleName}: $response"))
        }

    final override suspend fun read(response: Response) {
        readResponse(response).fold(
            { complete(it) },
            { completeExceptionally(it) }
        )
    }
}