//package com.whale.blog.repository;
//
//import com.whale.blog.domain.InmemmoryPost;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//
//// [요구사항 2] 데이터를 인메모리에 저장
//@Repository
//public class InmemmoryPostRepository {
//    // Map을 사용하여 데이터를 메모리에 저장
//    private static final Map<Long, InmemmoryPost> store = new LinkedHashMap<>();
//    private static long sequence = 0L; // ID 생성을 위한 시퀀스
//
//    /**
//     * 게시글 저장
//     */
//    public InmemmoryPost save(InmemmoryPost post) {
//        post.setId(++sequence); // 새 ID 할당
//        return store.put(post.getId(), post);
//    }
//
//    /**
//     * ID로 단건 조회 test
//     */
//    public Optional<InmemmoryPost> findById(Long id) {
//        return Optional.ofNullable(store.get(id));
//    }
//
//    /**
//     * 전체 목록 조회
//     */
//    public List<InmemmoryPost> findAll() {
//        return new ArrayList<>(store.values());
//    }
//}