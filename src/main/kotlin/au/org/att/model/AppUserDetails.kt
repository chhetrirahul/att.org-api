package au.org.att.model

import au.org.att.model.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AppUserDetails(private val user: User) : UserDetails {

    fun getUser() = user

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(user.role.uppercase()))
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.email
    }

    override fun isAccountNonExpired() = user.isActive

    override fun isAccountNonLocked() = user.isActive

    override fun isCredentialsNonExpired() = user.isActive

    override fun isEnabled() = user.isActive

}