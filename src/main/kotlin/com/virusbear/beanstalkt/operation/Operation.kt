package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.response.Response
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Deferred

interface Operation<out T>: Deferred<T> {
    suspend fun write(packet: BytePacketBuilder)
    suspend fun read(response: Response)
}