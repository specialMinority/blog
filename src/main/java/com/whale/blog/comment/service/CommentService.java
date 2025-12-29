package com.whale.blog.comment.service;

import com.whale.blog.comment.domain.Comment;
import com.whale.blog.comment.repository.CommentRepository;
import com.whale.blog.comment.repository.JpaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final JpaCommentRepository jpaCommentRepository;

    public CommentService(JpaCommentRepository jpaCommentRepository) {
        this.jpaCommentRepository = jpaCommentRepository;
    }


    // 댓글 등록
    public void create(Long postId, String content, String author, Long parentId) {
        Comment parent = null;
        if (parentId != null) {
            parent = jpaCommentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 없음"));
        }
        Comment comment = new Comment(postId, content, author, parent);
        jpaCommentRepository.save(comment);
    }

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<Comment> findAll(Long postId) {
        return jpaCommentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);
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
