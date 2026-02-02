package com.whale.blog.heart.controller;

import com.whale.blog.heart.service.HeartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Heart(좋아요) REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/posts")
public class HeartRestController {

    private final HeartService heartService;

    public HeartRestController(HeartService heartService) {
        this.heartService = heartService;
    }

    /**
     * [POST] 좋아요 토글
     * @param postId 게시글 ID
     * @param principal 현재 로그인한 사용자 정보
     * @return { "liked": true/false }
     */
    @PostMapping("/{postId}/heart")
    public ResponseEntity<Map<String, Object>> toggleHeart(@PathVariable Long postId, Principal principal) {
        // 로그인 확인
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        // 좋아요 토글 실행
        boolean liked = heartService.toggleHeartByLoginId(principal.getName(), postId);

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);

        return ResponseEntity.ok(response);
    }

    /**
     * [GET] 좋아요 상태 조회
     * @param postId 게시글 ID
     * @param principal 현재 로그인한 사용자 정보
     * @return { "liked": true/false }
     */
    @GetMapping("/{postId}/heart")
    public ResponseEntity<Map<String, Object>> getHeartStatus(@PathVariable Long postId, Principal principal) {
        // 로그인 확인
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        // 좋아요 상태 조회
        boolean liked = heartService.isLiked(principal.getName(), postId);

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);

        return ResponseEntity.ok(response);
    }
}
