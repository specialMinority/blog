package com.whale.blog.member.repository;

import com.whale.blog.member.domain.Member;

import java.util.*;

// @Repository // 사용 시 주석 해제
public class MemberInMemoryRepository implements MemberRepository {

    private static final Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return store.values().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}