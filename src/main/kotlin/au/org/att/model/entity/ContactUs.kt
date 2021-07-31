package au.org.att.model.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ContactUs(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    val fullAddress: String? = "",

    val contactNumber: String? = "",

    val email: String? = "",

    val facebookUrl: String? = "",

    val youtubeUrl: String? = "",

    val instaUrl: String? = ""

)
