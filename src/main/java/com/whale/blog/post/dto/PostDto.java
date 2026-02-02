package com.whale.blog.post.dto;

import com.whale.blog.comment.dto.CommentDto;
import com.whale.blog.post.domain.Post;

import java.util.List;

/**
 * Post 엔티티의 DTO (Data Transfer Object)
 * 엔티티를 API 응답이나 뷰에 전달할 때 사용합니다.
 */
public record PostDto(
        Long id,
        String title,
        String content,
        String author,
        int likeCount,
        List<CommentDto> comments
) {
    /**
     * Post 엔티티를 PostDto로 변환하는 정적 팩토리 메서드
     *
     * @param entity Post 엔티티
     * @return PostDto
     */
    public static PostDto from(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getAuthor(),
                entity.getLikeCount(),
                entity.getComments().stream()
                        .map(CommentDto::from)
                        .toList()
        );
    }
}