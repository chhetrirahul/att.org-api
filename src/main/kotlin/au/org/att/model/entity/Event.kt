package au.org.att.model.entity

import java.util.*
import javax.persistence.*

@Entity
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    var title: String,

    var content: String?,

    var location: String?,

    var startDate: Date,

    @Lob
    var image: ByteArray?,

    val createdDate: Date = Date()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
