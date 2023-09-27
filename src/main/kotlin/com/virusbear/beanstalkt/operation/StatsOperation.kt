package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.response.OkResponse
import com.virusbear.beanstalkt.response.Response
import io.ktor.utils.io.core.*

internal class StatsOperation(
    val id: UInt? = null,
    val tube: String? = null
): AbstractOperation<Map<String, String>>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("stats")

        id?.let {
            packet.append("-job $id")
        } ?: tube?.let {
            packet.append("-tube $tube")
        }

        packet.append("\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Map<String, String>> =
        when(response) {
            is OkResponse -> Result.success(response.stats)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}