package au.org.att.model.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ChangePasswordRequest(
    @field:NotBlank(message = "Password is mandatory")
    val old_password: String,
    @field:NotBlank(message = "Repeat password is mandatory")
    @field:Size(min = 8, message = "Use a minimum of 8 characters as new password")
    val new_password: String,
)
