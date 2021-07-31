package au.org.att.model.request

import javax.validation.constraints.NotBlank

data class BoardMemberRequest(
    @field:NotBlank(message = "Name is mandatory")
    val name: String,

    val email: String,

    val contactNumber: String,
)
