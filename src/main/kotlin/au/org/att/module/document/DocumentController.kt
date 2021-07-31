package au.org.att.module.document

import au.org.att.model.entity.Document
import au.org.att.model.entity.DocumentType
import au.org.att.model.response.AppResponse
import au.org.att.model.response.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class DocumentController(private val documentRepository: DocumentRepository) {

    @PostMapping("/admin/privacy_policy")
    fun addPrivacyPolicyDocument(@Valid @RequestBody document: Document): ResponseEntity<Any> {
        return saveDocument(DocumentType.PRIVACY_POLICY.name, document)
    }

    @PostMapping("/admin/terms_and_conditions")
    fun addTermsAndConditionsDocument(@Valid @RequestBody document: Document): ResponseEntity<Any> {
        return saveDocument(DocumentType.TERMS_AND_CONDITIONS.name, document)
    }

    private fun saveDocument(documentTypeName: String, document: Document): ResponseEntity<Any> {
        val optionalDocument = documentRepository.findByDocumentType(documentTypeName)
        return if (optionalDocument.isPresent) {
            val documentInDb = optionalDocument.get()
            documentInDb.content = document.content
            ResponseEntity(AppResponse(documentRepository.save(documentInDb)), HttpStatus.OK)
        } else {
            document.documentType = documentTypeName
            ResponseEntity(AppResponse(documentRepository.save(document)), HttpStatus.OK)
        }
    }

    @GetMapping("/privacy_policy")
    fun getPrivacyPolicyDocument(): ResponseEntity<Any> {
        val optionalDocument = documentRepository.findByDocumentType(DocumentType.PRIVACY_POLICY.name)
        return if (optionalDocument.isPresent) {
            ResponseEntity(
                AppResponse(optionalDocument.get()),
                HttpStatus.OK
            )
        } else {
            ResponseEntity(
                AppResponse(null, Status("Privacy policy document not found")),
                HttpStatus.NOT_FOUND
            )
        }
    }

    @GetMapping("/terms_and_conditions")
    fun getTermsAndConditionsDocument(): ResponseEntity<Any> {
        val optionalDocument = documentRepository.findByDocumentType(DocumentType.TERMS_AND_CONDITIONS.name)
        return if (optionalDocument.isPresent) {
            ResponseEntity(
                AppResponse(optionalDocument.get()),
                HttpStatus.OK
            )
        } else {
            ResponseEntity(
                AppResponse(null, Status("Terms and conditions document not found")),
                HttpStatus.NOT_FOUND
            )
        }
    }
}