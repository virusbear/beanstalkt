package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.response.Response
import com.virusbear.beanstalkt.response.UsingResponse
import io.ktor.utils.io.core.*

internal class ListTubeUsedOperation: AbstractOperation<String>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("list-tube-used\r\n")
    }

    override suspend fun readResponse(response: Response): Result<String> =
        when(response) {
            is UsingResponse -> Result.success(response.tube)
            else -> super.readResponse(response)
        }
}