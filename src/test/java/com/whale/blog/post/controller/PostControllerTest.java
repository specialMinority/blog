package com.whale.blog.post.controller;

import com.whale.blog.post.domain.Post;
import com.whale.blog.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * PostController 테스트
 * 특히 자동 작성자 기능(Principal을 통한 author 자동 설정)을 검증
 */
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private com.whale.blog.comment.service.CommentService commentService;

    @MockBean
    private com.whale.blog.heart.service.HeartService heartService;

    @Test
    @DisplayName("로그인한 사용자로 글 작성 시 작성자가 자동으로 설정된다")
    @WithMockUser(username = "testuser")
    void createPost_withAuthenticatedUser_shouldSetAuthorAutomatically() throws Exception {
        // When & Then
        mockMvc.perform(post("/posts")
                        .with(csrf())  // CSRF 토큰 추가
                        .param("title", "테스트 제목")
                        .param("content", "테스트 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/list"));

        // Verify: postService.create()가 호출될 때 author가 "testuser"로 설정되었는지 확인
        verify(postService).create(argThat(post ->
                "testuser".equals(post.getAuthor()) &&
                "테스트 제목".equals(post.getTitle()) &&
                "테스트 내용".equals(post.getContent())
        ));
    }

    @Test
    @DisplayName("비로그인 상태에서 글 작성 시 작성자가 null이다")
    void createPost_withoutAuthentication_shouldHaveNullAuthor() throws Exception {
        // When & Then
        mockMvc.perform(post("/posts")
                        .with(csrf())
                        .param("title", "테스트 제목")
                        .param("content", "테스트 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/list"));

        // Verify: author가 null인 상태로 저장되는지 확인
        verify(postService).create(argThat(post ->
                post.getAuthor() == null &&
                "테스트 제목".equals(post.getTitle())
        ));
    }

    @Test
    @DisplayName("다른 사용자로 로그인하면 각각의 작성자가 구분된다")
    @WithMockUser(username = "anotheruser")
    void createPost_withDifferentUser_shouldSetDifferentAuthor() throws Exception {
        // When & Then
        mockMvc.perform(post("/posts")
                        .with(csrf())
                        .param("title", "다른 사용자의 글")
                        .param("content", "내용"))
                .andExpect(status().is3xxRedirection());

        // Verify: author가 "anotheruser"로 설정되었는지 확인
        verify(postService).create(argThat(post ->
                "anotheruser".equals(post.getAuthor())
        ));
    }

    @Test
    @DisplayName("글쓰기 폼 페이지를 정상적으로 보여준다")
    void newForm_shouldReturnNewPostPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/posts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/new"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    @DisplayName("메인 페이지를 정상적으로 보여준다")
    void main_shouldReturnMainPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/main"));
    }
}
