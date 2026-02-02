package com.whale.blog.post.repository;

import com.whale.blog.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface JpaPostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContaining(String title, Pageable pageable);

    Page<Post> findByContentContaining(String content, Pageable pageable);

    Page<Post> findByAuthorContaining(String author, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p JOIN p.comments c WHERE c.content LIKE %:keyword%")
    Page<Post> findByCommentsContentContaining(@Param("keyword") String keyword, Pageable pageable);
}
