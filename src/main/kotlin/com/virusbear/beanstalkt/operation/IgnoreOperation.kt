package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotIgnoredException
import com.virusbear.beanstalkt.response.NotIgnoredResponse
import com.virusbear.beanstalkt.response.Response
import com.virusbear.beanstalkt.response.WatchingResponse
import io.ktor.utils.io.core.*

internal class IgnoreOperation(
    val tube: String
): AbstractOperation<UInt>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("ignore $tube\r\n")
    }

    override suspend fun readResponse(response: Response): Result<UInt> =
        when(response) {
            is WatchingResponse -> Result.success(response.count)
            is NotIgnoredResponse -> Result.failure(NotIgnoredException(tube))
            else -> super.readResponse(response)
        }
}