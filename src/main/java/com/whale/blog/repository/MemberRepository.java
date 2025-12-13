package com.whale.blog.repository;

import com.whale.blog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인 아이디로 회원 찾기 (로그인, 중복검사)
    Optional<Member> findByLoginId(String loginId);
}
