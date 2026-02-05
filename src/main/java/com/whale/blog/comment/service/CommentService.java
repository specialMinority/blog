package com.whale.blog.comment.service;

import com.whale.blog.comment.domain.Comment;
import com.whale.blog.comment.repository.CommentRepository;
import com.whale.blog.comment.repository.JpaCommentRepository;
import com.whale.blog.member.domain.Member;
import com.whale.blog.member.repository.JpaMemberRepository;
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
    private final JpaMemberRepository memberRepository;

    public CommentService(JpaCommentRepository jpaCommentRepository, JpaPostRepository postRepository, JpaMemberRepository memberRepository) {
        this.jpaCommentRepository = jpaCommentRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }


    // 댓글 등록
    public void create(Long postId, String content, String currentUserLoginId, Long parentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        
        // loginId로 Member 조회하여 nickname 가져오기
        Member member = memberRepository.findByLoginId(currentUserLoginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));
        
        Comment parent = null;
        if (parentId != null) {
            parent = jpaCommentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 없음"));
        }
        Comment comment = new Comment(post, content, member.getNickname(), parent);
        jpaCommentRepository.save(comment);
    }

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<Comment> findAll(Long postId) {
        return jpaCommentRepository.findByPost_IdAndParentIsNullOrderByCreatedAtAsc(postId);
    }

    //댓글 수정
    public Comment update(Long commentId, String newContent, String currentUserLoginId) {
        Comment comment = jpaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다"));

        // loginId를 nickname으로 변환하여 작성자 검증
        Member member = memberRepository.findByLoginId(currentUserLoginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));

        if (!comment.getAuthor().equals(member.getNickname())) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다");
        }

        comment.setContent(newContent);
        return jpaCommentRepository.save(comment);
    }

    //댓글 삭제
    public void delete(Long commentId, String currentUserLoginId) {
        Comment comment = jpaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다"));
        
        // loginId를 nickname으로 변환하여 작성자 검증
        Member member = memberRepository.findByLoginId(currentUserLoginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));
        
        if (!comment.getAuthor().equals(member.getNickname())) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다");
        }
        
        jpaCommentRepository.deleteById(commentId);
    }
}
