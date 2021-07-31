package au.org.att.model.response

import au.org.att.model.entity.Event

class EventResponse(
    val news: List<Event>,
    val totalElements: Int,
    val pageNumber: Int,
    val maxPage: Int
)