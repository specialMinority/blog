package com.whale.blog.heart.repository;

import com.whale.blog.heart.domain.Heart;
import com.whale.blog.member.domain.Member;
import com.whale.blog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    // 1. 이미 좋아요 눌렀는지 확인
    Optional<Heart> findByMemberAndPost(Member member, Post post);

    // 2. 해당 글의 좋아요 개수 카운트
    int countByPost(Post post);

    // 3. 존재 여부 boolean으로 반환
    boolean existsByMemberAndPost(Member member, Post post);

    // 4. 회원이 누른 좋아요 삭제 (회원 탈퇴 시 사용)
    void deleteByMember(Member member);

    // 5. 게시글 삭제 시 해당 글의 좋아요 삭제
    void deleteByPost(Post post);
}
