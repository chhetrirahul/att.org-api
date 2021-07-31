package au.org.att.security

import au.org.att.ROLE_ADMIN
import au.org.att.filter.JwtRequestFilter
import au.org.att.service.AppUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@EnableWebSecurity
class SecurityConfiguration(
    private val appUserDetailsService: AppUserDetailsService,
    private val jwtRequestFilter: JwtRequestFilter
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(appUserDetailsService)
    }

    override fun configure(http: HttpSecurity) {

        http.cors().configurationSource {
            CorsConfiguration().applyPermitDefaultValues()
        }
        http.csrf().disable()
        http.httpBasic().disable()
        http.authorizeRequests()
            .antMatchers("/api/admin/login").permitAll()
            .antMatchers("/api/admin/change_password").hasAuthority(ROLE_ADMIN)
            .antMatchers("/api/admin/logout").hasAuthority(ROLE_ADMIN)
            .antMatchers("/api/admin/me").hasAuthority(ROLE_ADMIN)
            .anyRequest().permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.exceptionHandling().accessDeniedHandler(getAccessDeniedHandler())

        http.exceptionHandling().authenticationEntryPoint(getCustomAuthEntryPoint())

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun getAccessDeniedHandler() = AppAccessDeniedHandler()

    @Bean
    fun getCustomAuthEntryPoint() = CustomAuthEntryPoint()

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun getPasswordEncoder() = BCryptPasswordEncoder()
}