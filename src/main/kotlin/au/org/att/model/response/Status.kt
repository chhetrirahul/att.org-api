package au.org.att.model.response

import java.util.*

data class Status(val date: Date, val message: String) {
    constructor(message: String) : this(date = Date(), message = message)
}
