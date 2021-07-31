package au.org.att.model.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class LoginRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is mandatory")
    val email: String,
    @field:NotBlank(message = "Password is mandatory")
    val password: String
)
