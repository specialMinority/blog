package com.whale.blog.repository;

import com.whale.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// @Repository
public interface JpaCommentRepository extends JpaRepository<Comment, Long>, CommentRepository {

    // JPA는 save, findById, deleteById 이미 가지고 있음
    List<Comment> findByPostId(Long postId);
}
