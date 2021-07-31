package au.org.att.model.response

import au.org.att.model.entity.User

data class LoginResponse(val user: User, val accessToken: String)
