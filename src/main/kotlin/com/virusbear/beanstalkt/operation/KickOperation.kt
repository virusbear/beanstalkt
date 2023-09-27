package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.KickedResponse
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.response.Response
import io.ktor.utils.io.core.*

internal class KickOperation(
    val bound: UInt? = null,
    val id: UInt? = null
): AbstractOperation<UInt>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("kick")

        bound?.let {
            packet.append(" $bound")
        } ?: id?.let {
            packet.append("-job $id")
        } ?: error("neither 'bound' nor 'id' specified for kick operation")

        packet.append("\r\n")
    }

    override suspend fun readResponse(response: Response): Result<UInt> =
        when(response) {
            is KickedResponse -> Result.success(response.count)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}