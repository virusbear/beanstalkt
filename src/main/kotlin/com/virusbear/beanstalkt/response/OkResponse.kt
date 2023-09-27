package com.virusbear.beanstalkt.response

import io.ktor.utils.io.*

internal class OkResponse(
    val stats: Map<String, String>
): Response {
    companion object: ResponseType {
        override val code: String = "OK"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response {
            val length = params[0].toInt()

            val yaml = channel.readPacket(length).readText()
            val stats = yaml.lines().filter { it != "---" && it.isNotEmpty() }.associate {
                if(it.startsWith("- ")) {
                    it.removePrefix("- ").trim().removeSurrounding("\"") to ""
                } else {
                    val (key, value) = it.split(": ", limit = 2)
                    key.trim().removeSurrounding("\"") to value.trim().removeSurrounding("\"")
                }
            }
            //Read last line break
            channel.readPacket(2).close()

            return OkResponse(stats)
        }
    }
}