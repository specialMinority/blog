package com.whale.blog.post.service;

import com.whale.blog.member.domain.Member;
import com.whale.blog.member.repository.JpaMemberRepository;
import com.whale.blog.post.domain.Post;
import com.whale.blog.post.domain.SearchType;
import com.whale.blog.post.dto.PostListDto;
import com.whale.blog.post.repository.JpaPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final JpaPostRepository postRepository;
    private final com.whale.blog.heart.repository.HeartRepository heartRepository;
    private final JpaMemberRepository memberRepository;

    public PostService(JpaPostRepository postRepository, com.whale.blog.heart.repository.HeartRepository heartRepository, JpaMemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.heartRepository = heartRepository;
        this.memberRepository = memberRepository;
    }

    public Post create(Post post, String currentUserLoginId) {
        if (post.getTitle() == null || post.getTitle().isBlank()) {
            throw new IllegalArgumentException("게시글 제목은 필수입니다.");
        }
        
        // loginId로 Member 조회하여 nickname 가져오기
        Member member = memberRepository.findByLoginId(currentUserLoginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));
        
        post.setAuthor(member.getNickname());
        return postRepository.save(post);
    }

    public List<Post> list() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<PostListDto> searchPosts(String searchKeyword, SearchType searchType, Pageable pageable) {
        // 검색어가 없으면 전체 조회
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return postRepository.findAll(pageable).map(PostListDto::from);
        }

        // 검색 타입별 조회
        return switch (searchType) {
            case TITLE -> postRepository.findByTitleContaining(searchKeyword, pageable).map(PostListDto::from);
            case CONTENT -> postRepository.findByContentContaining(searchKeyword, pageable).map(PostListDto::from);
            case AUTHOR -> postRepository.findByAuthorContaining(searchKeyword, pageable).map(PostListDto::from);
            case COMMENT -> postRepository.findByCommentsContentContaining(searchKeyword, pageable).map(PostListDto::from);
        };
    }

    /**
     * 게시글 수정
     * @param id 수정할 게시글 ID
     * @param post 수정할 게시글 정보
     * @return 수정된 Post 객체
     */
    public Post update(Long id, Post post) {
        Post existingPost = findById(id);
        
        if (post.getTitle() != null && !post.getTitle().isBlank()) {
            existingPost.setTitle(post.getTitle());
        }
        if (post.getContent() != null) {
            existingPost.setContent(post.getContent());
        }
        
        return postRepository.save(existingPost);
    }

    /**
     * 게시글 삭제
     * @param id 삭제할 게시글 ID
     */
    public void delete(Long id) {
        Post post = findById(id);
        
        // 좋아요 삭제 (FK 제약조건 해결)
        heartRepository.deleteByPost(post);
        
        // 게시글 삭제 (댓글은 Cascade로 자동 삭제됨)
        postRepository.delete(post);
    }
}