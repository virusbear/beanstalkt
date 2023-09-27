package com.virusbear.beanstalkt

import com.virusbear.beanstalkt.operation.Operation
import java.io.Closeable

interface Connection: Closeable {
    suspend fun <T> handle(operation: Operation<T>): Result<T>
}
