package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.*
import com.virusbear.beanstalkt.response.*
import io.ktor.utils.io.core.*
import kotlin.time.Duration

internal class PutOperation(
    val priority: UInt,
    val delay: Duration,
    val ttr: Duration,
    val data: ByteArray
): AbstractOperation<UInt>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("put $priority ${delay.inWholeSeconds} ${ttr.inWholeSeconds} ${data.size}\r\n")
        packet.writeFully(data)
        packet.append("\r\n")
    }

    override suspend fun readResponse(response: Response): Result<UInt> =
        when(response) {
            is InsertedResponse -> Result.success(response.id)
            is BuriedResponse -> Result.failure(BuriedException(response.id ?: UInt.MAX_VALUE))
            is ExpectedCrlfResponse -> Result.failure(ExpectedCrlfException())
            is JobTooBigResponse -> Result.failure(JobTooBigException())
            is DrainingResponse -> Result.failure(DrainingException())
            else -> super.readResponse(response)
        }
}

