package au.org.att.module.aboutUs

import au.org.att.model.entity.AboutUs
import au.org.att.model.response.AppResponse
import au.org.att.model.response.Status
import au.org.att.util.exceptions.BadRequestException
import au.org.att.util.saveFile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

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
        val fileName = file.originalFilename?.let { StringUtils.cleanPath(it) } ?: run {
            Date().time.toString()
        }

        val aboutUs: AboutUs = if (aboutUsRepository.count() > 0) {
            aboutUsRepository.findAll()[0]
        } else {
            AboutUs(0, null, null, null)
        }

        aboutUs.imageUrl = fileName

        val uploadDir = "images/"

        saveFile(uploadDir, fileName, file)

        return ResponseEntity(AppResponse(aboutUsRepository.save(aboutUs)), HttpStatus.OK)

    }

    @GetMapping("/about_us")
    fun getAboutUs(): ResponseEntity<Any> {
        return if (aboutUsRepository.count() > 0) {
            val aboutUs=aboutUsRepository.findAll()[0]
            val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
            aboutUs.imageUrl ="$baseUrl/images/${aboutUs.imageUrl}"
            ResponseEntity(AppResponse(aboutUs), HttpStatus.OK)
        } else {
            ResponseEntity(
                AppResponse(null, Status("Data not available. Insert data first.")), HttpStatus.NOT_FOUND
            )
        }
    }
}