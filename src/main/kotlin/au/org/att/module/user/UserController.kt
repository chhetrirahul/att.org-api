package au.org.att.module.user

import au.org.att.model.entity.AuthToken
import au.org.att.model.request.ChangePasswordRequest
import au.org.att.model.request.LoginRequest
import au.org.att.model.response.AppResponse
import au.org.att.model.response.LoginResponse
import au.org.att.model.response.Status
import au.org.att.module.auth.AuthRepository
import au.org.att.service.AppUserDetailsService
import au.org.att.util.exceptions.BadRequestException
import au.org.att.util.exceptions.InvalidOldPasswordException
import au.org.att.util.JwtUtil
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class UserController(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: AppUserDetailsService,
    private val authRepository: AuthRepository,
    private val jwtUtil: JwtUtil,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @PostMapping("/admin/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password
            )
        )
        val appUserDetails = userDetailsService.loadUserByUsername(loginRequest.email)

        val jwtToken = jwtUtil.generateToken(appUserDetails)

        var authToken: AuthToken?

        try {
            authToken = authRepository.findByUserId(appUserDetails.getUser().id)
            authToken.accessToken = jwtToken
        } catch (exception: EmptyResultDataAccessException) {
            authToken = AuthToken(0L, jwtToken)
            authToken.user = appUserDetails.getUser()
        }

        authRepository.save(authToken!!)

        return ResponseEntity(AppResponse(LoginResponse(appUserDetails.getUser(), jwtToken)), HttpStatus.OK)
    }

    @PostMapping("/admin/change_password")
    fun changePassword(
        @Valid @RequestBody changePasswordRequest: ChangePasswordRequest,
    ): ResponseEntity<Any> {
        val appUserDetails =
            userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().authentication.name)
        val user = appUserDetails.getUser()

        if (!bCryptPasswordEncoder.matches(changePasswordRequest.old_password, user.password)) {
            throw InvalidOldPasswordException("Incorrect old password")
        }

        val encodedNewPassword = bCryptPasswordEncoder.encode(changePasswordRequest.new_password)

        if (bCryptPasswordEncoder.matches(changePasswordRequest.new_password, user.password)) {
            throw BadRequestException("Old password cannot be used as new password")
        }
        user.password = encodedNewPassword
        userRepository.save(user)

        return ResponseEntity(AppResponse(null, Status("Change password successful")), HttpStatus.OK)
    }

    @GetMapping("/admin/logout")
    fun logout(): ResponseEntity<Any> {
        val appUserDetails =
            userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().authentication.name)
        val user = appUserDetails.getUser()
        val authToken = authRepository.findByUserId(user.id)
        authRepository.delete(authToken)
        return ResponseEntity(AppResponse(null, Status("User logged out successfully")), HttpStatus.OK)
    }

    @GetMapping("/admin/me")
    fun getProfile(): ResponseEntity<Any> {
        val appUserDetails =
            userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().authentication.name)
        val user = appUserDetails.getUser()
        return ResponseEntity(AppResponse(user), HttpStatus.OK)
    }
}