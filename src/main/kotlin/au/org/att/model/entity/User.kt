package au.org.att.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("email"))])
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val name: String,

    val email: String,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String,

    var isActive: Boolean,

    val role: String
) {
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    val authToken: AuthToken? = null
}