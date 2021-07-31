package au.org.att.util.exceptions

import au.org.att.model.response.AppResponse
import au.org.att.model.response.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class AppExceptionHandler {

    @ExceptionHandler(value = [AuthenticationException::class])
    fun handleAuthException(exception: AuthenticationException, request: WebRequest): ResponseEntity<Any> {
        val status = exception.message?.let { msg ->
            Status(msg)
        }
        return ResponseEntity(AppResponse<Any>(null, status), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<Any> {
        var errorString = ""
        exception.bindingResult.allErrors.let { errors ->
            errorString = errors.joinToString {
                "${it.defaultMessage}"
            }
        }
        val status = Status(errorString)
        return ResponseEntity(AppResponse<Any>(null, status), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [BadRequestException::class])
    fun handleBadRequestException(exception: BadRequestException): ResponseEntity<Any> {
        val status = exception.message?.let { Status(it) }
        return ResponseEntity(AppResponse<Any>(null, status), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [InvalidOldPasswordException::class])
    fun handleInvalidOldPasswordException(exception: InvalidOldPasswordException): ResponseEntity<Any> {
        val status = exception.message?.let { Status(it) }
        return ResponseEntity(AppResponse<Any>(null, status), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): ResponseEntity<Any> {
        return ResponseEntity(AppResponse<Any>(null, Status("Invalid request")), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [NoHandlerFoundException::class])
    fun handleNoHandlerFoundException(exception: NoHandlerFoundException): ResponseEntity<Any> {
        return ResponseEntity(AppResponse<Any>(null, Status("Not found")), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(exception: Exception, request: WebRequest): ResponseEntity<Any> {
        val status = exception.message?.let { msg ->
            Status(msg)
        }
        return ResponseEntity(AppResponse<Any>(null, status), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}