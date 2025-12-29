package com.whale.blog.post.service;

    import com.whale.blog.post.domain.Post;
    import com.whale.blog.post.repository.JpaPostRepository;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class PostService {

        private final JpaPostRepository postRepository;

        public PostService(JpaPostRepository postRepository) {
            this.postRepository = postRepository;
        }

        public Post create(Post post) {
            if (post.getTitle() == null || post.getTitle().isBlank()) {
                throw new IllegalArgumentException("게시글 제목은 필수입니다.");
            }
            return postRepository.save(post);
        }

        public List<Post> list() {
            return postRepository.findAll();
        }

        public Post findById(Long id) {
            return postRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다."));
        }
    }

