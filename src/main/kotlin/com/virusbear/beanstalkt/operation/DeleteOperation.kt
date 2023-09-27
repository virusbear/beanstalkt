package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.DeletedResponse
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.response.Response
import io.ktor.utils.io.core.*

internal class DeleteOperation(
    val id: UInt
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("delete $id\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Unit> =
        when(response) {
            is DeletedResponse -> Result.success(Unit)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}