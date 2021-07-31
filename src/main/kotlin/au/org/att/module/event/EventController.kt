package au.org.att.module.event

import au.org.att.DATE_FORMAT
import au.org.att.model.entity.Event
import au.org.att.model.request.EventRequest
import au.org.att.model.response.AppResponse
import au.org.att.model.response.EventResponse
import au.org.att.model.response.Status
import au.org.att.util.DateUtils
import au.org.att.util.exceptions.BadRequestException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class EventController(private val eventRepository: EventRepository) {

    @PostMapping("/admin/event")
    fun addEvent(@Valid @RequestBody eventRequest: EventRequest): ResponseEntity<Any> {
        if (eventRequest.title == null) {
            throw BadRequestException("Enter title and try again")
        }
        if (eventRequest.startDate == null) {
            throw BadRequestException("Enter start date and try again")
        }
        val date = DateUtils.parseDate(eventRequest.startDate)
            ?: throw BadRequestException("Enter date in format $DATE_FORMAT and try again")

        if (date.before(Date()))
            throw BadRequestException("Start date cannot before current date time")

        val event = Event(0, eventRequest.title, eventRequest.content, eventRequest.location, date, null)

        return ResponseEntity(AppResponse(eventRepository.save(event)), HttpStatus.OK)
    }


    @PostMapping("/admin/event/{id}")
    fun editEvent(@PathVariable("id") eventId: Long, @RequestBody eventRequest: EventRequest): ResponseEntity<Any> {
        val eventOptional = eventRepository.findById(eventId)

        if (!eventOptional.isPresent) {
            return ResponseEntity(AppResponse(null, Status("Event not found with id $eventId")), HttpStatus.BAD_REQUEST)
        }

        val eventInDb = eventOptional.get()

        if (eventRequest.title != null)
            eventInDb.title = eventRequest.title

        if (eventRequest.content != null)
            eventInDb.content = eventRequest.content

        if (eventRequest.location != null)
            eventInDb.location = eventRequest.location

        if (eventRequest.startDate != null) {
            val date = DateUtils.parseDate(eventRequest.startDate)
                ?: throw BadRequestException("Enter date in format $DATE_FORMAT and try again")

            if (date.before(Date()))
                throw BadRequestException("Start date cannot before current date time")
            eventInDb.startDate = date
        }

        return ResponseEntity(AppResponse(eventRepository.save(eventInDb)), HttpStatus.OK)
    }

    @PostMapping("/admin/event/image")
    fun addEventImage(
        @RequestParam("id") id: Long,
        @RequestParam("image_file") file: MultipartFile
    ): ResponseEntity<Any> {

        val eventOptional = eventRepository.findById(id)

        if (!eventOptional.isPresent) {
            return ResponseEntity(AppResponse(null, Status("Event not found with id $id")), HttpStatus.BAD_REQUEST)
        }

        val event = eventOptional.get()
        event.image = file.bytes

        return ResponseEntity(AppResponse(eventRepository.save(event)), HttpStatus.OK)
    }

    @GetMapping("/event")
    fun getAllEvent(
        @RequestParam(value = "page_number") pageNumber: Int,
        @RequestParam(value = "page_size") pageSize: Int
    ): ResponseEntity<Any> {

        val paging: Pageable = PageRequest.of(
            pageNumber, pageSize,
            Sort.by(Sort.Direction.ASC, "startDate")
        )

        val pagedResult = eventRepository.findAll(paging)

        return ResponseEntity(
            AppResponse(
                EventResponse(
                    pagedResult.toList(),
                    pagedResult.numberOfElements,
                    pageNumber,
                    pagedResult.totalPages
                )
            ), HttpStatus.OK
        )
    }

    @GetMapping("/event/{id}")
    fun getEventById(@PathVariable("id") eventId: Long): ResponseEntity<Any> {
        val eventOptional = eventRepository.findById(eventId)
        if (!eventOptional.isPresent)
            return ResponseEntity(AppResponse(null, Status("Event with id $eventId not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity(AppResponse(eventOptional.get()), HttpStatus.OK)
    }

    @DeleteMapping("/admin/event/{id}")
    fun deleteEventById(@PathVariable("id") eventId: Long): ResponseEntity<Any> {
        val eventOptional = eventRepository.findById(eventId)
        if (!eventOptional.isPresent)
            return ResponseEntity(AppResponse(null, Status("Event with id $eventId not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity(AppResponse(eventRepository.delete(eventOptional.get())), HttpStatus.OK)
    }
}