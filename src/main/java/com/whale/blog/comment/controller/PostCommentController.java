package com.whale.blog.comment.controller;

import com.whale.blog.comment.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostCommentController {

    private final CommentService commentService;

    public PostCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 등록
    @PostMapping("/posts/{postId}/comments")
    public String create(@PathVariable Long postId, 
                         @RequestParam String content, 
                         @RequestParam(required = false) Long parentId,
                         @AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        commentService.create(postId, content, loginId, parentId);
        return "redirect:/posts/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/comments/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        String currentUser = userDetails.getUsername();
        commentService.delete(id, currentUser);
        return "redirect:/posts/" + postId;
    }
}