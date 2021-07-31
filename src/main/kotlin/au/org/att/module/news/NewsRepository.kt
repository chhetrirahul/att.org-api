package au.org.att.module.news

import au.org.att.model.entity.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsRepository : JpaRepository<News, Long>