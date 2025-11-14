package com.whale.blog.web;

import com.whale.blog.domain.Post;
import com.whale.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // [요구사항 3] GET /posts : 게시글 목록 화면
    @GetMapping
    public String list(Model model) {
        List<Post> posts = postService.list();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    // [요구사항 1] GET /posts/new : 게시글 등록 폼
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new";
    }

    // [요구사항 1] POST /posts : 게시글 등록 처리
    @PostMapping
    public String create(@ModelAttribute Post post) {
        postService.create(post); // postService.create(post.getTitle(), post.getContent(), post.getAuthor()); 대신 post 객체 통째로 전달
        return "redirect:/posts";
    }

    // [요구사항 3] GET /posts/{id} : 게시글 상세 보기
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) { // 파라미터 이름도 'id'로 수정
        Post post = postService.get(id);
        model.addAttribute("post", post);
        return "posts/detail";
    }
}

//todo 컨트롤러에서 사용하는 에노테이션 리스트업,