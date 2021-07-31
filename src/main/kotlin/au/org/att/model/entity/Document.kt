package au.org.att.model.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotEmpty

@Entity
data class Document(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    var documentType: String? = "",

    @field:NotEmpty(message = "File content cannot be empty")
    var content: String
)

enum class DocumentType {
    PRIVACY_POLICY,
    TERMS_AND_CONDITIONS
}