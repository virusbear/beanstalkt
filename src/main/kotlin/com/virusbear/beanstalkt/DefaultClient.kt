package com.virusbear.beanstalkt

import com.virusbear.beanstalkt.operation.*
import com.virusbear.beanstalkt.response.*
import kotlin.time.Duration

@Suppress("SwallowedException")
class DefaultClient(
    host: String = "localhost",
    port: Int = 11300
): Client {
    private val connection: Connection =
        DefaultConnection(
            host,
            port,
            ResponseType.All
        )

    override suspend fun put(data: ByteArray, priority: UInt, delay: Duration, ttr: Duration): UInt =
        connection.handle(PutOperation(priority, delay, ttr, data)).getOrThrow()

    override suspend fun use(tube: String) {
        connection.handle(UseOperation(tube)).getOrThrow()
    }

    override suspend fun reserve(): Job =
        connection.handle(ReserveOperation()).getOrThrow()

    override suspend fun reserveWithTimeout(timeout: Duration): Job =
        connection.handle(ReserveOperation(timeout = timeout)).getOrThrow()

    override suspend fun reserveJob(id: UInt): Job =
        connection.handle(ReserveOperation(jobId = id)).getOrThrow()

    override suspend fun delete(id: UInt): Boolean =
        try {
            connection.handle(DeleteOperation(id)).getOrThrow()
            true
        } catch(ex: NotFoundException) {
            false
        }

    override suspend fun release(id: UInt, priority: UInt, delay: Duration): Boolean =
        try {
            connection.handle(ReleaseOperation(id, priority, delay)).getOrThrow()
            true
        } catch(ex: NotFoundException) {
            false
        }

    override suspend fun bury(id: UInt, priority: UInt): Boolean =
        try {
            connection.handle(BuryOperation(id, priority)).getOrThrow()
            true
        } catch(ex: NotFoundException) {
            false
        }

    override suspend fun touch(id: UInt): Boolean =
        try {
            connection.handle(TouchOperation(id)).getOrThrow()
            true
        } catch(ex: NotFoundException) {
            false
        }

    override suspend fun watch(tube: String) {
        connection.handle(WatchOperation(tube)).getOrThrow()
    }

    override suspend fun ignore(tube: String): Boolean =
        try {
            connection.handle(IgnoreOperation(tube)).getOrThrow()
            true
        } catch(ex: NotIgnoredException) {
            false
        }

    override suspend fun peek(id: UInt): Job? =
        try {
            connection.handle(PeekOperation(id = id)).getOrThrow()
        } catch(ex: NotFoundException) {
            null
        }

    override suspend fun peekReady(): Job? =
        try {
            connection.handle(PeekOperation(state = JobState.Ready)).getOrThrow()
        } catch(ex: NotFoundException) {
            null
        }

    override suspend fun peekDelayed(): Job? =
        try {
            connection.handle(PeekOperation(state = JobState.Delayed)).getOrThrow()
        } catch(ex: NotFoundException) {
            null
        }

    override suspend fun peekBuried(): Job? =
        try {
            connection.handle(PeekOperation(state = JobState.Buried)).getOrThrow()
        } catch(ex: NotFoundException) {
            null
        }

    override suspend fun kick(bound: UInt): UInt =
        connection.handle(KickOperation(bound = bound)).getOrThrow()

    override suspend fun kickJob(id: UInt): Boolean =
        try {
            connection.handle(KickOperation(id = id)).getOrThrow()
            true
        } catch(ex: NotFoundException) {
            false
        }

    override suspend fun statsJob(id: UInt): Map<String, String> =
        connection.handle(StatsOperation(id = id)).getOrThrow()

    override suspend fun statsTube(tube: String): Map<String, String> =
        connection.handle(StatsOperation(tube = tube)).getOrThrow()

    override suspend fun stats(): Map<String, String> =
        connection.handle(StatsOperation()).getOrThrow()

    override suspend fun listTubes(): List<String> =
        connection.handle(ListTubesOperation()).getOrThrow()

    override suspend fun listTubeUsed(): String =
        connection.handle(ListTubeUsedOperation()).getOrThrow()

    override suspend fun listTubesWatched(): List<String> =
        connection.handle(ListTubesOperation(watched = true)).getOrThrow()

    override suspend fun pauseTube(tubeName: String, delay: Duration): Boolean =
        try {
            connection.handle(PausedOperation(tubeName, delay)).getOrThrow()
            true
        } catch (ex: NotFoundException) {
            false
        }

    override fun close() {
        connection.close()
    }
}
