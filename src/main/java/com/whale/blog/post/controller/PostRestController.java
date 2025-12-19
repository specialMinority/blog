package com.whale.blog.post.controller;

// 1. [수정] Post 대신 InmemmoryPost를 임포트합니다.
import com.whale.blog.post.domain.InmemmoryPost;
import com.whale.blog.post.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [API 컨트롤러] - InmemmoryPost 사용 버전
 */
@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    /**
     * [GET] 전체 목록 조회
     * 반환 타입: List<InmemmoryPost>
     */
    @GetMapping
    public List<InmemmoryPost> list() {
        // PostService.list()가 List<InmemmoryPost>를 반환하므로 그대로 리턴합니다.
        return postService.list();
    }

    /**
     * [GET] 상세 조회
     * 반환 타입: InmemmoryPost
     */
    @GetMapping("/{id}")
    public InmemmoryPost detail(@PathVariable Long id) {
        return postService.get(id);
    }

    /**
     * [POST] 새 글 등록
     * 매개변수 및 반환 타입: InmemmoryPost
     */
    @PostMapping
    public InmemmoryPost create(@RequestBody InmemmoryPost post) {
        // @RequestBody를 통해 JSON 데이터를 InmemmoryPost 객체로 받습니다.
        return postService.create(post);
    }
}