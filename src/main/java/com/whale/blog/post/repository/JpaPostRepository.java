package com.whale.blog.post.repository;

import com.whale.blog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(String author);
}