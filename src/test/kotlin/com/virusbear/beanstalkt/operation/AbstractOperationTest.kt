package com.virusbear.beanstalkt.operation

import com.virusbear.beanstalkt.BadFormatException
import com.virusbear.beanstalkt.InternalErrorException
import com.virusbear.beanstalkt.OutOfMemoryException
import com.virusbear.beanstalkt.UnknownCommandException
import com.virusbear.beanstalkt.response.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.streams.asStream
import kotlin.test.*

class AbstractOperationTest {
    companion object {
        @JvmStatic
        fun provideDefaultErrorResponses(): Stream<Arguments> = sequence {
            yield(arguments(OutOfMemoryResponse, OutOfMemoryException::class))
            yield(arguments(InternalErrorResponse, InternalErrorException::class))
            yield(arguments(BadFormatResponse, BadFormatException::class))
            yield(arguments(UnknownCommandResponse, UnknownCommandException::class))
        }.asStream()
    }

    @ParameterizedTest(name = "test error response returns {1}")
    @MethodSource("provideDefaultErrorResponses")
    fun `test default error responses result in exception`(response: Response, error: KClass<*>): Unit = runBlocking {
        val op = object: AbstractOperation<Unit>() {
            override suspend fun write(packet: BytePacketBuilder) { }
        }

        val result = op.readResponse(response)
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals(error, exception::class)
    }

    @Test
    fun `test unknown response throws runtime exception`(): Unit = runBlocking {
        val op = object: AbstractOperation<Unit>() {
            override suspend fun write(packet: BytePacketBuilder) { }
        }
        val response = object: Response { }

        val result = op.readResponse(response)
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
    }

    @Test
    fun `test deferred is completed after read call`(): Unit = runBlocking {
        val op = object: AbstractOperation<Unit>() {
            override suspend fun write(packet: BytePacketBuilder) { }

            override suspend fun readResponse(response: Response): Result<Unit> {
                return Result.success(Unit)
            }
        }

        op.read(OutOfMemoryResponse)

        try {
            withTimeout(100) {
                op.await()
            }
        } catch(ex: TimeoutCancellationException) {
            fail("deferred did not complete")
        }
    }
}