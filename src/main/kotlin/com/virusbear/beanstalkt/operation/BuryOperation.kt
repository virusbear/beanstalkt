package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.BuriedResponse
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.response.Response
import io.ktor.utils.io.core.*

internal class BuryOperation(
    val id: UInt,
    val priority: UInt
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("bury $id $priority\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Unit> =
        when(response) {
            is BuriedResponse -> Result.success(Unit)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}