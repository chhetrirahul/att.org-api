package au.org.att.security

import au.org.att.DATE_FORMAT
import au.org.att.model.response.AppResponse
import au.org.att.model.response.Status
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        val objectMapper = ObjectMapper()
        response?.status = HttpStatus.FORBIDDEN.value()
        response?.contentType = MediaType.APPLICATION_JSON.toString()
        val out = response?.outputStream
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        objectMapper.dateFormat = dateFormat
        objectMapper.writeValue(out, AppResponse(null, Status("Access denied")))
        out?.flush()
    }

}