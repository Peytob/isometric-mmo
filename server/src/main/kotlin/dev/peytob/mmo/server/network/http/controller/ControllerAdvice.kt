package dev.peytob.mmo.server.network.http.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ResponseStatusException
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

    @ExceptionHandler(ResponseStatusException::class)
    fun responseStatusException(exception: ResponseStatusException): ResponseEntity<ErrorResponse> {
        if (exception.statusCode.is5xxServerError) {
            log.error("Conflict during creating new resource: {}", exception.message, exception)
        }

        if (log.isDebugEnabled && exception.statusCode.is4xxClientError) {
            log.debug("Handled response status exception with 4xx code: ", exception)
        }

        val response = errorResponse(
            message = exception.message,
            code = exception.statusCode
        )

        return ResponseEntity(response, exception.statusCode)
    }

    @ExceptionHandler(HttpClientErrorException::class)
    fun httpClientErrorException(exception: HttpClientErrorException): ResponseEntity<ErrorResponse> {
        log.error("Bad http response from integration with code {}: {}", exception.statusCode, exception.message, exception)
        val response = errorResponse(
            message = exception.message,
            code = HttpStatus.BAD_GATEWAY,
        )
        return ResponseEntity(response, HttpStatus.BAD_GATEWAY)
    }

    private fun errorResponse(message: String?, code: HttpStatusCode) = ErrorResponse(
        message = message ?: "",
        code = code,
        timestamp = Instant.now()
    )

    data class ErrorResponse(
        val message: String,
        val code: HttpStatusCode,
        val timestamp: Instant
    )
}