package com.whale.blog.post.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * String to SearchType 변환기
 * 대소문자 구분 없이 변환 가능하게 합니다.
 */
@Component
public class StringToSearchTypeConverter implements Converter<String, SearchType> {

    @Override
    public SearchType convert(String source) {
        if (source == null || source.isBlank()) {
            return SearchType.TITLE; // 기본값
        }
        try {
            return SearchType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SearchType.TITLE; // 잘못된 값이면 기본값
        }
    }
}
