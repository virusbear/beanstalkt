package com.virusbear.beanstalkt

class DisconnectedException: RuntimeException("Disconnected from beanstalkd server")
class InvalidResponseException: RuntimeException("The server responded with an invalid response")
class UnknownResponseException(val code: String): RuntimeException("The server responded with an unknown response: $code")

class OutOfMemoryException: RuntimeException("Beanstalkd out of memory")
class InternalErrorException: RuntimeException("Beanstalkd encountered an internal error")
class BadFormatException: RuntimeException("Command malformed")
class UnknownCommandException: RuntimeException("Command unknown")
class ExpectedCrlfException: RuntimeException("Expected Crlf")
class JobTooBigException: RuntimeException("Job too big")

class DrainingException: RuntimeException("Draining")
class BuriedException(val id: UInt): RuntimeException("Job $id is buried")

class DeadlineSoonException: RuntimeException("TTR approaches deadline")
class TimedOutException: RuntimeException("Request timed out")
class NotFoundException: RuntimeException("Not found")
class NotIgnoredException(val tube: String): RuntimeException("Tube $tube not ignored")
