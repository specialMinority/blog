package com.whale.blog.repository;

import com.whale.blog.domain.InmemmoryPost;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class SqlPostRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqlPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<InmemmoryPost> postRowMapper() {
        return (rs, rowNum) -> {
            InmemmoryPost post = new InmemmoryPost();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setAuthor(rs.getString("author"));
            return post;
        };
    }

    public InmemmoryPost save(InmemmoryPost post) {
        String sql = "INSERT INTO post (title, content, author) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getAuthor());
            return ps;
        }, keyHolder);

        long newId = keyHolder.getKey().longValue();
        post.setId(newId);

        return post;
    }

    public Optional<InmemmoryPost> findById(Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";
        // [오타 수정 완료] InmommoryPost -> InmemmoryPost
        List<InmemmoryPost> result = jdbcTemplate.query(sql, postRowMapper(), id);
        return result.stream().findAny();
    }

    public List<InmemmoryPost> findAll() {
        String sql = "SELECT * FROM post";
        return jdbcTemplate.query(sql, postRowMapper());
    }
}