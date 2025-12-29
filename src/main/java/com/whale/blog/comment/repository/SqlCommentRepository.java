//package com.whale.blog.comment.repository;
//
//import com.whale.blog.comment.domain.Comment;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.stereotype.Repository;
//
//import java.sql.PreparedStatement;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public class SqlCommentRepository implements CommentRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//
//    public SqlCommentRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    private RowMapper<Comment> commentRowMapper() {
//        return (rs, rowNum) -> {
//            Comment comment = new Comment();
//            comment.setId(rs.getLong("id"));
//            comment.setPostId(rs.getLong("post_id"));
//            comment.setContent(rs.getString("content"));
//            comment.setAuthor(rs.getString("author"));
//            return comment;
//        };
//    }
//
//    @Override
//    public Comment save(Comment comment) {
//        // ID가 있으면 수정(Update), 없으면 등록
//        if (comment.getId() != null) {
//            String sql = "UPDATE comment SET content = ? WHERE id = ?";
//            jdbcTemplate.update(sql, comment.getContent(), comment.getId());
//            return comment;
//        }
//
//        // 등록
//        String sql = "INSERT INTO comment (post_id, content, author) VALUES (?, ?, ?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(con -> {
//            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
//            ps.setLong(1, comment.getPostId());
//            ps.setString(2, comment.getContent());
//            ps.setString(3, comment.getAuthor());
//            return ps;
//        }, keyHolder);
//
//        comment.setId(keyHolder.getKey().longValue());
//        return comment;
//    }
//
//    @Override
//    public List<Comment> findByPostId(Long postId) {
//        String sql = "SELECT * FROM comment WHERE post_id = ?";
//        return jdbcTemplate.query(sql, commentRowMapper(), postId);
//    }
//
//    @Override
//    public Optional<Comment> findById(Long id) {
//        String sql = "SELECT * FROM comment WHERE id = ?";
//        List<Comment> result = jdbcTemplate.query(sql, commentRowMapper(), id);
//        return result.stream().findAny();
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        String sql = "DELETE FROM comment WHERE id = ?";
//        jdbcTemplate.update(sql, id);
//    }
//}
