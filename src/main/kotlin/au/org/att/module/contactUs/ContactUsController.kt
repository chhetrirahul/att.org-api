package au.org.att.module.contactUs

import au.org.att.model.entity.ContactUs
import au.org.att.model.response.AppResponse
import au.org.att.model.response.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/api")
class ContactUsController(private val contactUsRepository: ContactUsRepository) {

    @PostMapping("/admin/contact_us")
    fun addContactUsInfo(@RequestBody contactUs: ContactUs): ResponseEntity<Any> {
        if (contactUsRepository.count() > 0) {
            contactUs.id = contactUsRepository.findAll()[0].id
        }
        return ResponseEntity(AppResponse(contactUsRepository.save(contactUs)), HttpStatus.OK)
    }

    @GetMapping("/contact_us")
    fun getContactUsInfo(): ResponseEntity<Any> {
        return if (contactUsRepository.count() > 0) {
            ResponseEntity(AppResponse(contactUsRepository.findAll()[0]), HttpStatus.OK)
        } else {
            ResponseEntity(AppResponse(null, Status("Contact information not found")), HttpStatus.NOT_FOUND)
        }
    }
}