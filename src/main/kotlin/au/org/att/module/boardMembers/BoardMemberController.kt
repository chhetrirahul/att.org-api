package au.org.att.module.boardMembers

import au.org.att.model.entity.BoardMember
import au.org.att.model.request.BoardMemberRequest
import au.org.att.model.request.EditBoardMemberRequest
import au.org.att.model.response.AppResponse
import au.org.att.model.response.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class BoardMemberController(
    private val boardMemberRepository: BoardMemberRepository,
) {
    @PostMapping("/admin/board_members")
    fun addBoardMember(@Valid @RequestBody boardMemberRequest: BoardMemberRequest): ResponseEntity<Any> {
        val boardMember = BoardMember(
            0, boardMemberRequest.name,
            boardMemberRequest.email,
            boardMemberRequest.contactNumber, null
        )
        return ResponseEntity(AppResponse(boardMemberRepository.save(boardMember)), HttpStatus.OK)
    }

    @PostMapping("/admin/board_members/image")
    fun addBoardMemberImage(
        @RequestParam("id") id: Long,
        @RequestParam("image_file") file: MultipartFile
    ): ResponseEntity<Any> {

        val boardMemberOptional = boardMemberRepository.findById(id)

        if (!boardMemberOptional.isPresent) {
            return ResponseEntity(AppResponse(null, Status("User not found with id $id")), HttpStatus.BAD_REQUEST)
        }

        val boardMember = boardMemberOptional.get()
        boardMember.image = file.bytes

        return ResponseEntity(AppResponse(boardMemberRepository.save(boardMember)), HttpStatus.OK)
    }

    @PostMapping("/admin/board_members/{id}")
    fun editBoardMember(
        @PathVariable("id") id: Long,
        @Valid @RequestBody editBoardMemberRequest: EditBoardMemberRequest
    ): ResponseEntity<Any> {
        val boardMemberOptional = boardMemberRepository.findById(id)

        return if (boardMemberOptional.isPresent) {
            val boardMember = boardMemberOptional.get()
            editBoardMemberRequest.name?.let {
                boardMember.name = it
            }
            editBoardMemberRequest.email?.let {
                boardMember.email = it
            }
            editBoardMemberRequest.contactNumber?.let {
                boardMember.contactNumber = it
            }
            return ResponseEntity(AppResponse(boardMemberRepository.save(boardMember)), HttpStatus.OK)
        } else {
            ResponseEntity(AppResponse(null, Status("User not found with id $id")), HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/board_members")
    fun getAllBoardMembers(): ResponseEntity<Any> {
        val boardMemberList = boardMemberRepository.findAll()
        return ResponseEntity(AppResponse(boardMemberList), HttpStatus.OK)
    }

    @GetMapping("/board_members/{id}")
    fun getBoardMemberById(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val boardMember = boardMemberRepository.findById(id)
        return if (boardMember.isPresent)
            ResponseEntity(AppResponse(boardMember), HttpStatus.OK)
        else
            ResponseEntity(AppResponse(null, Status("User not found with id $id")), HttpStatus.NOT_FOUND)
    }
}