package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.response.Response
import com.virusbear.beanstalkt.response.TouchedResponse
import io.ktor.utils.io.core.*

internal class TouchOperation(
    val id: UInt
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("touche $id\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Unit> =
        when(response) {
            is TouchedResponse -> Result.success(Unit)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}