package com.whale.blog.service;

import com.whale.blog.domain.Comment;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommentService {

    // 댓글 등록
    public Comment create(Long postId, String content, String author) {
        Comment comment = new Comment(postId, content, author);
        //todo 나중에 Repository 호출해야 함
        System.out.println("댓글 저장됨: " + content);
        return comment;
    }

    // 댓글 목록 조회
    public List<Comment> findAll(Long postId) {
        //todo 나중에 Repository findAllByPostId(postId)를 호출해야 함
        return Collections.emptyList();
    }

    //댓글 수정
    public Comment update(Long commentId, String content) {
        //todo DB에서 commentId로 찾아서 수정해야 함
        return new Comment();
    }

    //댓글 삭제
    public void delete(Long commentId) {
        //todo DB에서 삭제해야 함
    }
}
