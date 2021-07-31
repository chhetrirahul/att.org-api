package au.org.att.model.response

data class AppResponse<out T>(val data: T?, val status: Status?) {
    constructor(data: T?) : this(data = data, status = Status("Success"))
}
