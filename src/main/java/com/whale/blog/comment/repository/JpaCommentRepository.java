package com.whale.blog.comment.repository;

import com.whale.blog.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCommentRepository extends JpaRepository<Comment, Long> {

    // JPA는 save, findById, deleteById 이미 가지고 있음
    List<Comment> findByPostId(Long postId);

    // 게시글의 "최상위 댓글"만 가져오기, 자식 댓글은 JPA가 알아서 children 리스트에 담아줌
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtAsc(Long postId);
}
