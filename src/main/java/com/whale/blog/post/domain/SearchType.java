package com.whale.blog.post.domain;

/**
 * 게시글 검색 타입을 정의하는 Enum
 */
public enum SearchType {
    TITLE("제목"),
    CONTENT("내용"),
    AUTHOR("작성자"),
    COMMENT("댓글");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
