//JPA

//    package com.whale.blog.service;
//
//    import com.whale.blog.post.domain.InmemmoryPost;
//    import com.whale.blog.repository.JpaPostRepository;
//    import org.springframework.stereotype.Service;
//
//    import java.util.List;
//
//    @Service
//    public class PostService {
//
//        private final JpaPostRepository postRepository;
//
//        public PostService(JpaPostRepository postRepository) {
//            this.postRepository = postRepository;
//        }
//
//        public InmemmoryPost create(InmemmoryPost post) {
//            if (post.getTitle() == null || post.getTitle().isBlank()) {
//                throw new IllegalArgumentException("게시글 제목은 필수입니다.");
//            }
//            return postRepository.save(post);
//        }
//
//        public List<InmemmoryPost> list() {
//            return postRepository.findAll();
//        }
//
//        public InmemmoryPost get(Long id) {
//            return postRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다."));
//        }
//    }

//sql

package com.whale.blog.post.service;

import com.whale.blog.post.domain.InmemmoryPost;
// 1. [중요] SqlPostRepository를 임포트합니다.
import com.whale.blog.post.repository.SqlPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    // 2. 주입받는 타입을 SqlPostRepository로 정확히 지정합니다.
    private final SqlPostRepository postRepository;

    public PostService(SqlPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public InmemmoryPost create(InmemmoryPost post) {
        if (post.getTitle() == null || post.getTitle().isBlank()) {
            throw new IllegalArgumentException("게시글 제목은 필수입니다.");
        }
        return postRepository.save(post);
    }

    public List<InmemmoryPost> list() {
        return postRepository.findAll();
    }

    public InmemmoryPost get(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다."));
    }
}