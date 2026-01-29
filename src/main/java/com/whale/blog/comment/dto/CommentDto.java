package com.whale.blog.comment.dto;

import com.whale.blog.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Comment 엔티티의 DTO (Data Transfer Object)
 * 대댓글(children)도 재귀적으로 포함합니다.
 */
public record CommentDto(
        Long id,
        String content,
        String author,
        LocalDateTime createdAt,
        Long parentId,
        List<CommentDto> children
) {
    /**
     * Comment 엔티티를 CommentDto로 변환하는 정적 팩토리 메서드
     *
     * @param entity Comment 엔티티
     * @return CommentDto
     */
    public static CommentDto from(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getContent(),
                entity.getAuthor(),
                entity.getCreatedAt(),
                entity.getParent() != null ? entity.getParent().getId() : null,
                entity.getChildren().stream()
                        .map(CommentDto::from)
                        .toList()
        );
    }
}
