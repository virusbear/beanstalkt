package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.BuriedException
import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.BuriedResponse
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.response.ReleasedResponse
import com.virusbear.beanstalkt.response.Response
import io.ktor.utils.io.core.*
import kotlin.time.Duration

internal class ReleaseOperation(
    val id: UInt,
    val priority: UInt,
    val delay: Duration
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("release $id $priority ${delay.inWholeSeconds}\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Unit> =
        when(response) {
            is ReleasedResponse -> Result.success(Unit)
            is BuriedResponse -> Result.failure(BuriedException(response.id ?: id))
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}