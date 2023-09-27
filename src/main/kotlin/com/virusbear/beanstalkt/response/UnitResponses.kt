package com.virusbear.beanstalkt.response

internal object OutOfMemoryResponse: UnitResponse("OUT_OF_MEMORY")
internal object InternalErrorResponse: UnitResponse("INTERNAL_ERROR")
internal object BadFormatResponse: UnitResponse("BAD_FORMAT")
internal object UnknownCommandResponse: UnitResponse("UNKNOWN_COMMAND")

internal object ExpectedCrlfResponse: UnitResponse("EXPECTED_CRLF")
internal object JobTooBigResponse: UnitResponse("JOB_TOO_BIG")
internal object DrainingResponse: UnitResponse("DRAINING")

internal object DeadlineSoonResponse: UnitResponse("DEADLINE_SOON")
internal object TimedOutResponse: UnitResponse("TIMED_OUT")
internal object NotFoundResponse: UnitResponse("NOT_FOUND")

internal object DeletedResponse: UnitResponse("DELETED")
internal object ReleasedResponse: UnitResponse("RELEASED")
internal object TouchedResponse: UnitResponse("TOUCHED")
internal object NotIgnoredResponse: UnitResponse("NOT_IGNORED")
internal object PausedResponse: UnitResponse("PAUSED")