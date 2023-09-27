package com.virusbear.beanstalkt

import java.io.Closeable
import kotlin.time.Duration

@Suppress("TooManyFunctions")
interface Client: Closeable {
    suspend fun put(
        data: ByteArray,
        priority: UInt = 0u,
        delay: Duration = Duration.ZERO,
        ttr: Duration = Duration.INFINITE
    ): UInt
    suspend fun use(tube: String)
    suspend fun reserve(): Job
    suspend fun reserveWithTimeout(timeout: Duration): Job
    suspend fun reserveJob(id: UInt): Job
    suspend fun delete(id: UInt): Boolean
    suspend fun release(id: UInt, priority: UInt = 0u, delay: Duration = Duration.ZERO): Boolean
    suspend fun bury(id: UInt, priority: UInt = 0u): Boolean
    suspend fun touch(id: UInt): Boolean
    suspend fun watch(tube: String)
    suspend fun ignore(tube: String): Boolean
    suspend fun peek(id: UInt): Job?
    suspend fun peekReady(): Job?
    suspend fun peekDelayed(): Job?
    suspend fun peekBuried(): Job?
    suspend fun kick(bound: UInt): UInt
    suspend fun kickJob(id: UInt): Boolean
    suspend fun statsJob(id: UInt): Map<String, String>
    suspend fun statsTube(tube: String): Map<String, String>
    suspend fun stats(): Map<String, String>
    suspend fun listTubes(): List<String>
    suspend fun listTubeUsed(): String
    suspend fun listTubesWatched(): List<String>
    suspend fun pauseTube(tubeName: String, delay: Duration = Duration.ZERO): Boolean
}
