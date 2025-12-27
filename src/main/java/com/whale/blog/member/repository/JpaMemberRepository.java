package com.whale.blog.member.repository;

import com.whale.blog.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // 스프링 빈으로 등록 + JPA 예외를 스프링 예외로 변환
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em; // JPA의 모든 동작은 엔티티 매니저를 통해 이루어집니다.

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member); // 영구 저장(persist). 쿼리를 작성할 필요 없이 객체를 넣으면 됩니다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id); // PK를 통한 조회는 find() 메서드 하나로 끝납니다.
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        // PK가 아닌 필드로 조회할 때는 JPQL이라는 객체 지향 쿼리를 작성해야 합니다.
        List<Member> result = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public void delete(Member member) {
        em.remove(member);
    }
}