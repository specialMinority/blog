package com.whale.blog.comment.controller;

import com.whale.blog.comment.dto.CommentDto;
import com.whale.blog.comment.service.CommentService;
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
     * @param requestBody 댓글 정보 (content, author, parentId)
     */
    @PostMapping("/posts/{postId}/comments")
    public void create(@PathVariable Long postId, @RequestBody Map<String, Object> requestBody) {
        String content = (String) requestBody.get("content");
        String author = (String) requestBody.get("author");
        Long parentId = requestBody.get("parentId") != null 
            ? Long.valueOf(requestBody.get("parentId").toString()) 
            : null;
        
        commentService.create(postId, content, author, parentId);
    }

    /**
     * [PUT] 댓글 수정
     * @param id 댓글 ID
     * @param requestBody 수정할 내용
     * @return 수정된 댓글 DTO
     */
    @PutMapping("/comments/{id}")
    public CommentDto update(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String content = requestBody.get("content");
        return CommentDto.from(commentService.update(id, content));
    }

    /**
     * [DELETE] 댓글 삭제
     * @param id 댓글 ID
     */
    @DeleteMapping("/comments/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}
