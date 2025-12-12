package com.whale.blog.web;

import com.whale.blog.domain.Comment;
import com.whale.blog.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostCommentRestController {

    private final CommentService commentService;

    public PostCommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public List<Comment> getComments(@PathVariable long postId) {
        return commentService.findAll(postId);
    }

    // 댓글 등록
    @PostMapping("/posts/{postId}/comments")
    public Comment create(@PathVariable long postId, @RequestBody Comment comment) {
        //@RequestBody: JSON 데이터를 Comment 객체로 반환
        return commentService.create(postId, comment.getContent(), comment.getAuthor());
    }

    //댓글 수정
    @PutMapping("/comments/{id}")
    public Comment update(@PathVariable long id, @RequestBody Comment comment) {
        return commentService.update(id, comment.getContent());
    }

    //댓글 삭제
    @DeleteMapping("/comments/{id}")
    public void delete(@PathVariable long id) {
        commentService.delete(id);
    }
}
