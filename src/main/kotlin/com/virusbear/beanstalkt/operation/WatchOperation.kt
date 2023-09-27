package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.response.Response
import com.virusbear.beanstalkt.response.WatchingResponse
import io.ktor.utils.io.core.*

internal class WatchOperation(
    val tube: String
): AbstractOperation<UInt>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("watch $tube\r\n")
    }

    override suspend fun readResponse(response: Response): Result<UInt> =
        when(response) {
            is WatchingResponse -> Result.success(response.count)
            else -> super.readResponse(response)
        }
}