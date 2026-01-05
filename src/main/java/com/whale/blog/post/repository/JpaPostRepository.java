package com.whale.blog.post.repository;

import com.whale.blog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContains(String title);
    List<Post> findAllByOrderByIdDesc(); // 최신순 조회
    List<Post> findAllByOrderByIdAsc(); // 오래된순 조회
}