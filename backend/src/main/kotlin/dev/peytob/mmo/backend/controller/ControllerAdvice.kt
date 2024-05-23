package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.exception.ResourceAlreadyExistsException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

private val log = LoggerFactory.getLogger(ControllerAdvice::class.java)

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun baseExceptionHandler(exception: Exception): ErrorResponse {
        log.error("Unhandled exception during executing request: {}", exception.message, exception)
        return errorResponse(
            message = exception.message,
            code = HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }

    @ExceptionHandler(ResourceAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun resourceAlreadyExistsException(exception: ResourceAlreadyExistsException): ErrorResponse {
        log.debug("Conflict during creating new resource: {}", exception.message, exception)
        return errorResponse(
            message = exception.message,
            code = HttpStatus.CONFLICT
        )
    }

    private fun errorResponse(message: String?, code: HttpStatus) = ErrorResponse(
        message = message ?: "",
        code = code,
        timestamp = Instant.now()
    )

    data class ErrorResponse(
        val message: String,
        val code: HttpStatus,
        val timestamp: Instant
    )
}
