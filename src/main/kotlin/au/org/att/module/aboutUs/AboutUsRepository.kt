package au.org.att.module.aboutUs

import au.org.att.model.entity.AboutUs
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AboutUsRepository : JpaRepository<AboutUs, Long>