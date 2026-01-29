package com.whale.blog.post.dto;

import com.whale.blog.post.domain.Post;

/**
 * 게시글 목록용 간단한 DTO
 * 댓글을 포함하지 않아 N+1 문제 방지
 */
public record PostListDto(
        Long id,
        String title,
        String content,
        String author,
        int likeCount
) {
    /**
     * Post 엔티티를 PostListDto로 변환
     */
    public static PostListDto from(Post entity) {
        return new PostListDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getAuthor(),
                entity.getLikeCount()
        );
    }
}
