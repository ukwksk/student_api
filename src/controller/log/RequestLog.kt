package co.ukwksk.log

import com.fasterxml.jackson.core.json.JsonWriteFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.server.engine.*
import org.slf4j.LoggerFactory

private val requestLogger = LoggerFactory.getLogger("interceptor.RequestLogger")

@EngineAPI
fun writeRequestLog(
    call: ApplicationCall,
    exception: Throwable? = null,
) {
    val log = RequestLogBuilder(call, exception).build()
    val msg = log.toJsonString()
    when {
        log.status < 400 -> requestLogger.info(msg)
        log.status < 500 -> requestLogger.warn(msg)
        else -> requestLogger.error(msg)
    }
}

@EngineAPI
internal class RequestLogBuilder(
    call: ApplicationCall,
    exception: Throwable?
) {
    private val log = Log(call.request, call.response, exception)

    inner class Log private constructor(
        val method: String,
        val uri: String,
        val parameters: Map<String, Any>?,
        val userAgent: String,
        val remoteAddr: String,
        val status: Int
    ) {
        constructor(
            req: ApplicationRequest,
            res: ApplicationResponse,
            exception: Throwable?,
        ) : this(
            method = req.httpMethod.value,
            uri = req.path(),
            parameters = createParameter(req.queryString()),
            userAgent = req.userAgent() ?: "-",
            remoteAddr = req.header("X-Forwarded-For") ?: "-",
            status = (res.status()
                ?: exception?.let { defaultExceptionStatusCode(it) }
                ?: HttpStatusCode.InternalServerError)
                .value,
        )

        fun toJsonString() = objectMapper.writeValueAsString(this)!!
    }

    fun build() = log

    companion object {
        private val objectMapper = JsonMapper.builder()
            .enable(JsonWriteFeature.ESCAPE_NON_ASCII)
            .build()

        private fun createParameter(
            queryString: String
        ): Map<String, String>? =
            queryString
                .takeIf { it.isNotEmpty() }
                ?.split("&")?.associate {
                    val keyValue = it.split("=")
                    keyValue[0] to keyValue[1]
                }
    }
}
