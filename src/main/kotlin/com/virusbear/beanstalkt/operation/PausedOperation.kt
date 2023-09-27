package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.NotFoundException
import com.virusbear.beanstalkt.response.NotFoundResponse
import com.virusbear.beanstalkt.response.PausedResponse
import com.virusbear.beanstalkt.response.Response
import io.ktor.utils.io.core.*
import kotlin.time.Duration

internal class PausedOperation(
    val tube: String,
    val delay: Duration
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("pause-tube $tube ${delay.inWholeSeconds}\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Unit> =
        when(response) {
            is PausedResponse -> Result.success(Unit)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}