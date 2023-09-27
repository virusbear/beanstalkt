package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.response.Response
import com.virusbear.beanstalkt.response.UsingResponse
import io.ktor.utils.io.core.*

internal class UseOperation(
    private val tube: String
): AbstractOperation<String>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("use $tube\r\n")
    }

    override suspend fun readResponse(response: Response): Result<String> =
        when(response) {
            is UsingResponse -> Result.success(response.tube)
            else -> super.readResponse(response)
        }
}