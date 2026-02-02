package com.whale.blog.post.controller;

// 1. [수정] Post 대신 InmemmoryPost를 임포트합니다.
import com.whale.blog.post.domain.Post;
import com.whale.blog.post.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [API 컨트롤러] - Post 사용 버전
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
     * 반환 타입: List<Post>
     */
    @GetMapping
    public List<Post> list() {
        // PostService.list()가 List<Post>를 반환하므로 그대로 리턴합니다.
        return postService.list();
    }

    /**
     * [GET] 상세 조회
     * 반환 타입: Post
     */
    @GetMapping("/{id}")
    public Post detail(@PathVariable Long id) {
        return postService.findById(id);
    }

    /**
     * [POST] 새 글 등록
     * 매개변수 및 반환 타입: Post
     */
    @PostMapping
    public Post create(@RequestBody Post post) {
        // @RequestBody를 통해 JSON 데이터를 Post 객체로 받습니다.
        return postService.create(post);
    }

    /**
     * [PUT] 게시글 수정
     * @param id 수정할 게시글 ID
     * @param post 수정할 게시글 정보
     * @return 수정된 Post 객체
     */
    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @RequestBody Post post) {
        return postService.update(id, post);
    }

    /**
     * [DELETE] 게시글 삭제
     * @param id 삭제할 게시글 ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}