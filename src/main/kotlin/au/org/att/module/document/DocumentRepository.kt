package au.org.att.module.document

import au.org.att.model.entity.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DocumentRepository : JpaRepository<Document, Long> {
    fun findByDocumentType(documentType: String): Optional<Document>
}