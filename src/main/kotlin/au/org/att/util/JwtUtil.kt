package au.org.att.util

import au.org.att.TOKEN_EXPIRY_NUM_DAYS
import au.org.att.model.AppUserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Service
class JwtUtil {

    private val SECRET_KEY = "secret"

    fun extractUsername(token: String): String = extractClaim(token, Claims::getSubject)

    private fun extractExpiration(token: String) = extractClaim(token, Claims::getExpiration)

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String) = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body

    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())

    fun generateToken(userDetails: AppUserDetails): String {
        return createToken(HashMap<String, JvmType.Object>(), userDetails.username)
    }

    private fun createToken(claims: Map<String, JvmType.Object>, subject: String): String =
        Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + TOKEN_EXPIRY_NUM_DAYS * 24 * 60 * 60 * 1000L))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact()

    fun validateToken(token: String, userDetails: AppUserDetails): Boolean {
        val userEmail = extractUsername(token)
        return userEmail == userDetails.username && !isTokenExpired(token)
    }
}