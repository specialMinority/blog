package com.whale.blog.web;

import com.whale.blog.service.CommentService;
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
    // 역할: 댓글을 저장하고 다시 게시글 상세 페이지로 리다이렉트
    @PostMapping("/posts/{postId}/comments")
    public String create(@PathVariable Long postId, @RequestParam String comment, @RequestParam String author) {

        //1. 서비스에게 댓글 저장을 시킨다.
        commentService.create(postId, comment, author);

        //2. 저장이 끝나면 보던 게시글로 돌아간다.
        return "redirect:/posts/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/comments/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam Long postId) {
        commentService.delete(id);
        return "redirect:/posts/" + postId;
    }
}
