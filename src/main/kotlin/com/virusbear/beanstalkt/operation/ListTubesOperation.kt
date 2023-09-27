package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.response.OkResponse
import com.virusbear.beanstalkt.response.Response
import io.ktor.utils.io.core.*

internal class ListTubesOperation(
    val watched: Boolean = false
): AbstractOperation<List<String>>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("list-tubes")
        if(watched) {
            packet.append("-watched")
        }
        packet.append("\r\n")
    }

    override suspend fun readResponse(response: Response): Result<List<String>> =
        when(response) {
            is OkResponse -> Result.success(response.stats.keys.toList())
            else -> super.readResponse(response)
        }
}