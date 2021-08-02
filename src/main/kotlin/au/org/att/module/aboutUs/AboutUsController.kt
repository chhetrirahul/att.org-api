package au.org.att.module.aboutUs

import au.org.att.model.entity.AboutUs
import au.org.att.model.response.AppResponse
import au.org.att.model.response.Status
import au.org.att.util.exceptions.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class AboutUsController(private val aboutUsRepository: AboutUsRepository) {

    @PostMapping("/admin/about_us")
    fun addAboutUs(@RequestBody aboutUs: AboutUs): ResponseEntity<Any> {
        if (aboutUs.aboutText == null) {
            throw BadRequestException("Field 'aboutText' is required.")
        }

        return if (aboutUsRepository.count() > 0) {
            val aboutUsInDb = aboutUsRepository.findAll()[0]
            aboutUsInDb.aboutText = aboutUs.aboutText
            ResponseEntity(AppResponse(aboutUsRepository.save(aboutUsInDb)), HttpStatus.OK)
        } else {
            ResponseEntity(AppResponse(aboutUsRepository.save(aboutUs)), HttpStatus.OK)
        }
    }

    @PostMapping("/admin/about_us/image")
    fun addAboutUsImage(@RequestParam("image_file") file: MultipartFile): ResponseEntity<Any> {
        return if (aboutUsRepository.count() > 0) {
            val aboutUs = aboutUsRepository.findAll()[0]
            aboutUs.image = file.bytes
            ResponseEntity(AppResponse(aboutUsRepository.save(aboutUs)), HttpStatus.OK)
        } else {
            val aboutUs = AboutUs(0, null, file.bytes)
            ResponseEntity(AppResponse(aboutUsRepository.save(aboutUs)), HttpStatus.OK)
        }
    }

    @GetMapping("/about_us")
    fun getAboutUs(): ResponseEntity<Any> {
        return if (aboutUsRepository.count() > 0) {
            ResponseEntity(AppResponse(aboutUsRepository.findAll()[0]), HttpStatus.OK)
        } else {
            ResponseEntity(
                AppResponse(null, Status("Data not available. Insert data first.")), HttpStatus.NOT_FOUND
            )
        }
    }
}