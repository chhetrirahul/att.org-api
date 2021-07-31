package au.org.att.module.news

import au.org.att.model.entity.News
import au.org.att.model.response.AppResponse
import au.org.att.model.response.NewsResponse
import au.org.att.model.response.Status
import au.org.att.util.exceptions.BadRequestException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class NewsController(private val newsRepository: NewsRepository) {

    @PostMapping("/admin/news")
    fun addNews(@RequestBody news: News): ResponseEntity<Any> {
        if (news.title == null) {
            throw BadRequestException("Enter title and try again")
        }

        return ResponseEntity(AppResponse(newsRepository.save(news)), HttpStatus.OK)
    }

    @PostMapping("/admin/news/{id}")
    fun editNews(@PathVariable("id") newsId: Long, @RequestBody news: News): ResponseEntity<Any> {
        val newsOptional = newsRepository.findById(newsId)

        if (!newsOptional.isPresent) {
            return ResponseEntity(AppResponse(null, Status("News not found with id $newsId")), HttpStatus.BAD_REQUEST)
        }

        val newsInDb = newsOptional.get()

        if (news.title != null) {
            newsInDb.title = news.title
        }

        if (news.content != null) {
            newsInDb.content = news.content
        }

        return ResponseEntity(AppResponse(newsRepository.save(newsInDb)), HttpStatus.OK)
    }


    @PostMapping("/admin/news/image")
    fun addNewsImage(
        @RequestParam("id") id: Long,
        @RequestParam("image_file") file: MultipartFile
    ): ResponseEntity<Any> {

        val newsOptional = newsRepository.findById(id)

        if (!newsOptional.isPresent) {
            return ResponseEntity(AppResponse(null, Status("News not found with id $id")), HttpStatus.BAD_REQUEST)
        }

        val news = newsOptional.get()
        news.image = file.bytes

        return ResponseEntity(AppResponse(newsRepository.save(news)), HttpStatus.OK)
    }


    @GetMapping("/news")
    fun getAllNews(
        @RequestParam(value = "page_number") pageNumber: Int,
        @RequestParam(value = "page_size") pageSize: Int
    ): ResponseEntity<Any> {

        val paging: Pageable = PageRequest.of(
            pageNumber, pageSize,
            Sort.by(Sort.Direction.DESC, "createdDate")
        )

        val pagedResult = newsRepository.findAll(paging)

        return ResponseEntity(
            AppResponse(
                NewsResponse(
                    pagedResult.toList(),
                    pagedResult.numberOfElements,
                    pageNumber,
                    pagedResult.totalPages
                )
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("/news/{id}")
    fun getNewsById(@PathVariable("id") newsId: Long): ResponseEntity<Any> {
        val newsOptional = newsRepository.findById(newsId)
        if (!newsOptional.isPresent)
            return ResponseEntity(AppResponse(null, Status("News with id $newsId not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity(AppResponse(newsOptional.get()), HttpStatus.OK)
    }

    @DeleteMapping("/admin/news/{id}")
    fun deleteNewsById(@PathVariable("id") newsId: Long): ResponseEntity<Any> {
        val newsOptional = newsRepository.findById(newsId)
        if (!newsOptional.isPresent)
            return ResponseEntity(AppResponse(null, Status("News with id $newsId not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity(AppResponse(newsRepository.delete(newsOptional.get())), HttpStatus.OK)
    }
}