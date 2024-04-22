package dev.peytob.mmo.backend.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

private val log = LoggerFactory.getLogger(ControllerAdvice::class.java)

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler
    fun baseExceptionHandler(exception: Exception): ErrorResponse {
        log.error("Unhandled exception during executing request: {}", exception.message, exception)
        return ErrorResponse(
            message = exception.message ?: "",
            code = HttpStatus.INTERNAL_SERVER_ERROR,
            timestamp = Instant.now()
        )
    }

    data class ErrorResponse(
        val message: String,
        val code: HttpStatus,
        val timestamp: Instant
    )
}
