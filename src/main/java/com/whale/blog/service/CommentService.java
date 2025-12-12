package com.whale.blog.service;

import com.whale.blog.domain.Comment;
import com.whale.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(@Qualifier("sqlCommentRepository") CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 댓글 등록
    public Comment create(Long postId, String content, String author) {
        Comment comment = new Comment(postId, content, author);
        return commentRepository.save(comment);
    }

    // 댓글 목록 조회
    public List<Comment> findAll(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    //댓글 수정
    public Comment update(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    //댓글 삭제
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
