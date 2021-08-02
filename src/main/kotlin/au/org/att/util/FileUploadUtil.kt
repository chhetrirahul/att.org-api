package au.org.att.util

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

fun saveFile(uploadDir: String, fileName: String, multipartFile: MultipartFile) {
    val uploadPath = Paths.get(uploadDir)

    if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath)
    }

    try {
        val inputStream = multipartFile.inputStream
        val filePath = uploadPath.resolve(fileName)
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
    } catch (exception: IOException) {
        throw IOException("Could not save image file: $fileName", exception)
    }
}