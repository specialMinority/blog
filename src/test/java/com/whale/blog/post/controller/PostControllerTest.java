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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.springframework.security.authentication.TestingAuthenticationToken;
import com.whale.blog.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;

/**
 * PostController 테스트
 * 특히 자동 작성자 기능(Principal을 통한 author 자동 설정)을 검증
 */
@WebMvcTest(PostController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private com.whale.blog.comment.service.CommentService commentService;

    @MockBean
    private com.whale.blog.heart.service.HeartService heartService;

    @MockBean
    private com.whale.blog.member.repository.JpaMemberRepository memberRepository;
    
    @BeforeEach
    void setUp() {
        Member testMember = new Member("testuser", "password", "테스터");
        given(memberRepository.findByLoginId("testuser")).willReturn(Optional.of(testMember));
        
        Member anotherMember = new Member("anotheruser", "password", "다른유저");
        given(memberRepository.findByLoginId("anotheruser")).willReturn(Optional.of(anotherMember));
    }

    @Test
    @DisplayName("로그인한 사용자로 글 작성 시 작성자가 자동으로 설정된다")
    @WithMockUser(username = "testuser")
    void createPost_withAuthenticatedUser_shouldSetAuthorAutomatically() throws Exception {
        // When & Then
        mockMvc.perform(post("/posts")
                        .with(csrf())
                        .principal(new TestingAuthenticationToken("testuser", null)) // Principal 직접 주입
                        .param("title", "테스트 제목")
                        .param("content", "테스트 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/list"));
        
        // Verify: postService.create() was called with loginId
        verify(postService).create(
                argThat(post ->
                        post.getTitle().equals("테스트 제목") &&
                        post.getContent().equals("테스트 내용")
                ),
                eq("testuser")
        );
    }



    @Test
    @DisplayName("비로그인 상태에서 글 작성 시 예외가 발생한다")
    void createPost_withoutAuthentication_shouldThrowException() {
        // When & Then
        Assertions.assertThatThrownBy(() -> 
            mockMvc.perform(post("/posts")
                            .with(csrf())
                            .param("title", "테스트 제목")
                            .param("content", "테스트 내용"))
        ).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("다른 사용자로 로그인하면 각각의 작성자가 구분된다")
    @WithMockUser(username = "anotheruser")
    void createPost_withDifferentUser_shouldSetDifferentAuthor() throws Exception {
        // When & Then
        mockMvc.perform(post("/posts")
                        .with(csrf())
                        .principal(new TestingAuthenticationToken("anotheruser", null)) // Principal 직접 주입
                        .param("title", "다른 사용자의 글")
                        .param("content", "내용"))
                .andExpect(status().is3xxRedirection());

        // Verify: postService.create() was called with anotheruser's loginId
        verify(postService).create(
                argThat(post ->
                        post.getTitle().equals("다른 사용자의 글")
                ),
                eq("anotheruser")
        );
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
