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
import com.whale.blog.comment.service.CommentService;
import com.whale.blog.heart.service.HeartService;
import com.whale.blog.member.repository.JpaMemberRepository;
import com.whale.blog.post.domain.Post;
import com.whale.blog.post.domain.SearchType;
import com.whale.blog.post.dto.PostListDto;
import com.whale.blog.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService; // 댓글 기능
    private final HeartService heartService; // 좋아요 기능
    private final JpaMemberRepository memberRepository; // Member 조회용

    public PostController(PostService postService, CommentService commentService, HeartService heartService, JpaMemberRepository memberRepository) {
        this.postService = postService;
        this.commentService = commentService;
        this.heartService = heartService;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String main() {
        return "posts/main";
    }

    @GetMapping("/list")
    public String list(
            @RequestParam(value = "searchType", defaultValue = "TITLE") SearchType searchType,
            @RequestParam(value = "searchText", required = false) String searchKeyword,
            @PageableDefault(size = 5, sort = "likeCount", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        // 1. 별도의 Sort, PageRequest 생성 로직이 필요 없음 (pageable에 이미 다 담겨 있음)
        // 2. 서비스 호출
        Page<PostListDto> posts = postService.searchPosts(searchKeyword, searchType, pageable);

        model.addAttribute("posts", posts);
        return "posts/list"; // 뷰 이름
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new";
    }

    @PostMapping
    public String create(@ModelAttribute Post post, Principal principal) {
        // 로그인한 사용자의 loginId를 서비스로 전달
        if (principal != null) {
            String loginId = principal.getName();
            postService.create(post, loginId);
        } else {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return "redirect:/posts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, Principal principal) {
        // 1. 게시글 조회
        Post post = postService.findById(id);

        // 2. 로그인 여부에 따라 좋아요 확인 및 작성자 본인 확인
        boolean isLiked = false;
        boolean isAuthor = false;
        String currentUserNickname = null;
        
        if (principal != null) {
            String loginId = principal.getName();
            isLiked = heartService.isLiked(loginId, id);
            isAuthor = post.getAuthor() != null && post.getAuthor().equals(loginId);
            
            // 현재 사용자의 nickname 조회
            memberRepository.findByLoginId(loginId).ifPresent(member -> {
                model.addAttribute("currentUserNickname", member.getNickname());
            });
        }

        model.addAttribute("post", post);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isAuthor", isAuthor); // 본인 확인용 플래그

        // 3. 댓글 조회 등...
        List<Comment> comments = commentService.findAll(id);
        model.addAttribute("comments", comments);

        return "posts/detail";
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Principal principal) {
        Post post = postService.findById(id);
        
        // 작성자 검증
        if (principal == null || !post.getAuthor().equals(principal.getName())) {
            throw new IllegalArgumentException("修正権限がありません。");
        }

        model.addAttribute("post", post);
        return "posts/edit"; // 수정 폼 템플릿
    }

    // 수정 처리
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute Post post, Principal principal) {
        Post originalPost = postService.findById(id);

        // 작성자 검증
        if (principal == null || !originalPost.getAuthor().equals(principal.getName())) {
            throw new IllegalArgumentException("修正権限がありません。");
        }

        postService.update(id, post);
        return "redirect:/posts/" + id;
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        Post post = postService.findById(id);

        // 작성자 검증
        if (principal == null || !post.getAuthor().equals(principal.getName())) {
            throw new IllegalArgumentException("削除権限がありません。");
        }

        postService.delete(id);
        return "redirect:/posts/list";
    }
}