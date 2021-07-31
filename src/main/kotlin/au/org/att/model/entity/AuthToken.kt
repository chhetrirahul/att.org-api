package au.org.att.model.entity

import javax.persistence.*

@Entity
data class AuthToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var accessToken: String
) {
    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User? = null
}
