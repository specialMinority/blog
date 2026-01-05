package com.whale.blog.comment.service;

import com.whale.blog.comment.domain.Comment;
import com.whale.blog.comment.repository.CommentRepository;
import com.whale.blog.comment.repository.JpaCommentRepository;
import com.whale.blog.post.domain.Post;
import com.whale.blog.post.repository.JpaPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final JpaCommentRepository jpaCommentRepository;
    private final JpaPostRepository postRepository;

    public CommentService(JpaCommentRepository jpaCommentRepository, JpaPostRepository postRepository) {
        this.jpaCommentRepository = jpaCommentRepository;
        this.postRepository = postRepository;
    }


    // 댓글 등록
    public void create(Long postId, String content, String author, Long parentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        Comment parent = null;
        if (parentId != null) {
            parent = jpaCommentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 없음"));
        }
        Comment comment = new Comment(post, content, author, parent);
        jpaCommentRepository.save(comment);
    }

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<Comment> findAll(Long postId) {
        return jpaCommentRepository.findByPost_IdAndParentIsNullOrderByCreatedAtAsc(postId);
    }

    //댓글 수정
    public Comment update(Long commentId, String newContent) {
        Comment comment = jpaCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setContent(newContent);
        return jpaCommentRepository.save(comment);
    }

    //댓글 삭제
    public void delete(Long commentId) {
        jpaCommentRepository.deleteById(commentId);
    }
}
