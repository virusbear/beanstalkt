package com.virusbear.beanstalkt

import com.virusbear.beanstalkt.operation.Operation
import com.virusbear.beanstalkt.response.ResponseType
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

internal class DefaultConnection(
    host: String,
    port: Int,
    private val responseTypes: Set<ResponseType>
): Connection, CoroutineScope {
    override val coroutineContext: CoroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private var socket: Socket

    private var readChannel: ByteReadChannel
    private var writeChannel: ByteWriteChannel

    private val isConnected: Boolean
        get() = !(socket.isClosed || readChannel.isClosedForRead || writeChannel.isClosedForWrite)

    private val operationQueue = Channel<Operation<*>>(capacity = Channel.BUFFERED)

    private val operationExecutorJob = launch {
        for(operation in operationQueue) {
            execute(operation)
        }
    }

    init {
        socket = runBlocking { aSocket(selectorManager).tcp().connect(host, port) }

        readChannel = socket.openReadChannel()
        writeChannel = socket.openWriteChannel()
    }

    override suspend fun <T> handle(operation: Operation<T>): Result<T> {
        operationQueue.send(operation)

        return try {
            Result.success(operation.await())
        } catch(ex: Throwable) {
            Result.failure(ex)
        }
    }

    override fun close() {
        operationQueue.close()
        operationExecutorJob.cancel()
        socket.close()
    }

    private suspend fun <T> execute(operation: Operation<T>) {
        if(!isConnected) {
            throw DisconnectedException()
        }

        writeChannel.writePacketSuspend {
            operation.write(this)
        }
        writeChannel.flush()

        val responseParams = readChannel.readUTF8Line()?.split(" ") ?: emptyList()
        if(responseParams.isEmpty()) {
            throw InvalidResponseException()
        }

        val code = responseParams.first()
        val params = responseParams.drop(1)
        val responseType = responseTypes.firstOrNull { it.code == code } ?: throw UnknownResponseException(code)

        val response = responseType.read(params, readChannel)
        operation.read(response)
    }
}
