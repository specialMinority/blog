package com.whale.blog.heart.service;

import com.whale.blog.heart.domain.Heart;
import com.whale.blog.heart.repository.HeartRepository;
import com.whale.blog.member.domain.Member;
import com.whale.blog.member.repository.MemberRepository;
import com.whale.blog.post.domain.Post;
import com.whale.blog.post.repository.JpaPostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final JpaPostRepository postRepository;

    public boolean toggleHeartByLoginId(String loginId, Long postId) {
        // 1. 로그인 아이디로 회원 조회
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("회원 정보가 없음"));

        // 2. 게시글 조회
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않음"));

        // 3. 이미 좋아요 눌렀는지 확인
        Optional<Heart> heartOpt = heartRepository.findByMemberAndPost(member, post);
        if (heartOpt.isPresent()) {
            heartRepository.delete(heartOpt.get());
            return false;
        } else {
            heartRepository.save(new Heart(member, post));
            return true;
        }
    }

    @Transactional(readOnly = true)
    public boolean isLiked(String loginId, Long postId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없음"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않음"));

        return heartRepository.existsByMemberAndPost(member, post);
    }

    // 특정 게시글 좋아요 개수 조회
    @Transactional(readOnly = true)
    public int countHeart(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return heartRepository.countByPost(post);
    }
}
