package com.whale.blog.comment.repository;

import com.whale.blog.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// @Repository
public interface JpaCommentRepository extends JpaRepository<Comment, Long>, CommentRepository {

    // JPA는 save, findById, deleteById 이미 가지고 있음
    List<Comment> findByPostId(Long postId);
}
