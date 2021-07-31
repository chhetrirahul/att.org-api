package au.org.att.module.user

import au.org.att.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(userEmail: String): Optional<User>

    fun existsByEmail(userEmail: String): Boolean
}