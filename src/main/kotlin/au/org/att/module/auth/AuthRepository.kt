package au.org.att.module.auth

import au.org.att.model.entity.AuthToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository : JpaRepository<AuthToken, Long> {
    fun findByUserId(userId: Long): AuthToken
}