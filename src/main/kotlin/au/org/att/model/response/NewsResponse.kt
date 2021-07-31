package au.org.att.model.response

import au.org.att.model.entity.News

data class NewsResponse(
    val news: List<News>,
    val totalElements: Int,
    val pageNumber: Int,
    val maxPage: Int
)
