package au.org.att.service

import au.org.att.model.AppUserDetails
import au.org.att.module.user.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AppUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(email: String): AppUserDetails {
        val optionalUser = userRepository.findByEmail(email)
        optionalUser.orElseThrow {
            UsernameNotFoundException("User not found")
        }
        return AppUserDetails(optionalUser.get())
    }
}