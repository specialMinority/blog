package com.whale.blog.comment.repository;

import com.whale.blog.comment.domain.Comment;

import java.util.List;
import java.util.Optional;

// InMemory, SQL, JPA 공통 구현 인터페이스
public interface CommentRepository {

    // 저장
    Comment save(Comment comment);

    // 게시글 ID로 댓글 목록 찾기
    List<Comment> findByPostId(Long postId);

    // 댓글 ID로 댓글 목록 찾기
    Optional<Comment> findById(Long id);

    //삭제
    void deleteById(Long id);
}
