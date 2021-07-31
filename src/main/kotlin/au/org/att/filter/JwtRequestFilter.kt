package au.org.att.filter

import au.org.att.model.entity.AuthToken
import au.org.att.module.auth.AuthRepository
import au.org.att.service.AppUserDetailsService
import au.org.att.util.JwtUtil
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(
    private val userDetailsService: AppUserDetailsService,
    private val authRepository: AuthRepository,
    private val jwtUtil: JwtUtil
) :
    OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        var userEmail: String? = null
        var jwtToken = ""

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7)
            userEmail = jwtUtil.extractUsername(jwtToken)
        }

        if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            val appUserDetails = userDetailsService.loadUserByUsername(userEmail)

            var tokenInDb: AuthToken? = null

            try {
                tokenInDb = authRepository.findByUserId(appUserDetails.getUser().id)
            } catch (exception: EmptyResultDataAccessException) {

            }

            if (jwtToken == tokenInDb?.accessToken && jwtUtil.validateToken(jwtToken, appUserDetails)) {
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(appUserDetails, null, appUserDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }

        filterChain.doFilter(request, response)
    }
}