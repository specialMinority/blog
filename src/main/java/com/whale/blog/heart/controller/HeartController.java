package com.whale.blog.heart.controller;

import com.whale.blog.heart.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/posts/{postId}/like")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long postId, Principal principal) {
        // 로그인 안 한 경우
        if(principal == null) {
            return ResponseEntity.status(401).build();
        }

        // 좋아요 토글 실행(true or false)
        boolean liked = heartService.toggleHeartByLoginId(principal.getName(), postId);

        // JS에 보낼 JSON 데이터 반환
        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);

        return ResponseEntity.ok(response);
    }
}
