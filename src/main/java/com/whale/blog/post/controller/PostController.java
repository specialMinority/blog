//package com.whale.blog.web;
//
//import com.whale.blog.post.domain.InmemmoryPost;
//import com.whale.blog.post.service.PostService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/posts")
//public class PostController {
//
//    private final PostService postService;
//
//    public PostController(PostService postService) {
//        this.postService = postService;
//    }
//
//    // [요구사항 3] GET /posts : 게시글 목록 화면
//    @GetMapping
//    public String list(Model model) {
//        List<InmemmoryPost> posts = postService.list();
//        model.addAttribute("posts", posts);
//        return "posts/list";
//    }
//
//    // [요구사항 1] GET /posts/new : 게시글 등록 폼
//    @GetMapping("/new")
//    public String newForm(Model model) {
//        model.addAttribute("post", new InmemmoryPost());
//        return "posts/new";
//    }
//
//    // [요구사항 1] POST /posts : 게시글 등록 처리
//    @PostMapping
//    public String create(@ModelAttribute InmemmoryPost post) {
//        postService.create(post); // postService.create(post.getTitle(), post.getContent(), post.getAuthor()); 대신 post 객체 통째로 전달
//        return "redirect:/posts";
//    }
//
//    // [요구사항 3] GET /posts/{id} : 게시글 상세 보기
//    @GetMapping("/{id}")
//    public String detail(@PathVariable Long id, Model model) { // 파라미터 이름도 'id'로 수정
//        InmemmoryPost post = postService.get(id);
//        model.addAttribute("post", post);
//        return "posts/detail";
//    }
//}

//JPA

package com.whale.blog.post.controller;

import com.whale.blog.comment.domain.Comment;
import com.whale.blog.post.domain.InmemmoryPost;
import com.whale.blog.comment.service.CommentService;
import com.whale.blog.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService; // 댓글 기능

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public String list(Model model) {
        List<InmemmoryPost> posts = postService.list();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("post", new InmemmoryPost());
        return "posts/new";
    }

    @PostMapping
    public String create(@ModelAttribute InmemmoryPost post) {
        postService.create(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        // 1. 게시글 조회
        InmemmoryPost post = postService.get(id);
        model.addAttribute("post", post);

        // 2. 게시글에 달린 댓글 목록 조회
        List<Comment> comments = commentService.findAll(id);
        model.addAttribute("comments", comments);

        return "posts/detail";
    }
}