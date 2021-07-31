package au.org.att

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories(basePackages = ["au.org.att"])
class AttApiApplication

fun main(args: Array<String>) {
    runApplication<AttApiApplication>(*args)
}
