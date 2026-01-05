//package com.whale.blog.comment.repository;
//
//import com.whale.blog.comment.domain.Comment;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
////@Repository
//public class InMemoryCommentRepository implements CommentRepository {
//
//    private static final Map<Long, Comment> store = new HashMap<>();
//    private static long sequence = 0L;
//
//    @Override
//    public Comment save(Comment comment) {
//        // ID가 없으면 새로 생성, 있으면 그대로 둠
//        if(comment.getId() == null) {
//            comment.setId(++sequence);
//        }
//        store.put(comment.getId(), comment);
//        return comment;
//    }
//
//    @Override
//    public List<Comment> findByPostId(Long postId) {
//        return store.values().stream().filter(c -> c.getId().equals(postId)).collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<Comment> findById(Long id) {
//        return Optional.ofNullable(store.get(id));
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        store.remove(id);
//    }
//}
