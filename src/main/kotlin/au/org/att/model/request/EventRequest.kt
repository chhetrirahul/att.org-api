package au.org.att.model.request

import javax.validation.constraints.NotBlank

data class EventRequest(

    @field:NotBlank(message = "Title should not be empty")
    val title: String?,

    val content: String?,

    val location: String?,

    @field:NotBlank(message = "Start date should not be empty")
    val startDate: String?,
)
