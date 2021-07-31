package au.org.att.module.boardMembers

import au.org.att.model.entity.BoardMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardMemberRepository : JpaRepository<BoardMember, Long>