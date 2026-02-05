package com.whale.blog.comment.controller;

import com.whale.blog.comment.dto.CommentDto;
import com.whale.blog.comment.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostCommentRestController {

    private final CommentService commentService;

    public PostCommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * [GET] 댓글 목록 조회
     * @param postId 게시글 ID
     * @return 댓글 목록 (대댓글 포함)
     */
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.findAll(postId).stream()
                .map(CommentDto::from)
                .toList();
    }

    /**
     * [POST] 댓글 등록
     * @param postId 게시글 ID
     * @param requestBody 댓글 정보 (content, parentId)
     * @param userDetails 현재 로그인한 사용자 정보
     */
    @PostMapping("/posts/{postId}/comments")
    public void create(@PathVariable Long postId, 
                      @RequestBody Map<String, Object> requestBody,
                      @AuthenticationPrincipal UserDetails userDetails) {
        String content = (String) requestBody.get("content");
        String loginId = userDetails.getUsername();
        Long parentId = requestBody.get("parentId") != null 
            ? Long.valueOf(requestBody.get("parentId").toString()) 
            : null;
        
        commentService.create(postId, content, loginId, parentId);
    }

    /**
     * [PUT] 댓글 수정
     * @param id 댓글 ID
     * @param requestBody 수정할 내용
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 수정된 댓글 DTO
     */
    @PutMapping("/comments/{id}")
    public CommentDto update(@PathVariable Long id, 
                            @RequestBody Map<String, String> requestBody,
                            @AuthenticationPrincipal UserDetails userDetails) {
        String content = requestBody.get("content");
        String loginId = userDetails.getUsername();
        return CommentDto.from(commentService.update(id, content, loginId));
    }

    /**
     * [DELETE] 댓글 삭제
     * @param id 댓글 ID
     * @param userDetails 현재 로그인한 사용자 정보
     */
    @DeleteMapping("/comments/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String currentUser = userDetails.getUsername();
        commentService.delete(id, currentUser);
    }
}
