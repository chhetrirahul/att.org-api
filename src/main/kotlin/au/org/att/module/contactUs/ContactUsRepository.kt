package au.org.att.module.contactUs

import au.org.att.model.entity.ContactUs
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactUsRepository : JpaRepository<ContactUs, Long>