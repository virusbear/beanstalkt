package com.virusbear.beanstalkt.response

import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.streams.asStream
import kotlin.test.assertEquals

class UnitResponseTests {

    companion object {
        @JvmStatic
        fun provideUnitResponses(): Stream<Arguments> = sequence {
            yield(arguments(OutOfMemoryResponse, "OUT_OF_MEMORY", OutOfMemoryResponse::class))
            yield(arguments(InternalErrorResponse, "INTERNAL_ERROR", InternalErrorResponse::class))
            yield(arguments(BadFormatResponse, "BAD_FORMAT", BadFormatResponse::class))
            yield(arguments(UnknownCommandResponse, "UNKNOWN_COMMAND", UnknownCommandResponse::class))
            yield(arguments(ExpectedCrlfResponse, "EXPECTED_CRLF", ExpectedCrlfResponse::class))
            yield(arguments(JobTooBigResponse, "JOB_TOO_BIG", JobTooBigResponse::class))
            yield(arguments(DrainingResponse, "DRAINING", DrainingResponse::class))
            yield(arguments(DeadlineSoonResponse, "DEADLINE_SOON", DeadlineSoonResponse::class))
            yield(arguments(TimedOutResponse, "TIMED_OUT", TimedOutResponse::class))
            yield(arguments(NotFoundResponse, "NOT_FOUND", NotFoundResponse::class))
            yield(arguments(DeletedResponse, "DELETED", DeletedResponse::class))
            yield(arguments(ReleasedResponse, "RELEASED", ReleasedResponse::class))
            yield(arguments(TouchedResponse, "TOUCHED", TouchedResponse::class))
            yield(arguments(NotIgnoredResponse, "NOT_IGNORED", NotIgnoredResponse::class))
            yield(arguments(PausedResponse, "PAUSED", PausedResponse::class))
        }.asStream()
    }


    @ParameterizedTest(name = "code is {1}")
    @MethodSource("provideUnitResponses")
    fun testCode(responseType: ResponseType, code: String, responseClass: KClass<*>) {
        assertEquals(responseType.code, code)
    }

    @ParameterizedTest(name = "{1}: type returns itself")
    @MethodSource("provideUnitResponses")
    fun testTypeReturnsItself(responseType: ResponseType, code: String, responseClass: KClass<*>) = runBlocking {
        val params = emptyList<String>()
        val channel = ByteReadChannel(byteArrayOf())
        val response = responseType.read(params, channel)

        assertEquals(responseClass, response::class)
    }
}