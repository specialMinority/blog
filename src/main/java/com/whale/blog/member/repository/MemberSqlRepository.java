package com.whale.blog.member.repository;

import com.whale.blog.member.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Timestamp; // 날짜 변환용
import java.util.List;
import java.util.Optional;

//@Repository("sqlMemberRepository") // 이름을 지정해두면 Service에서 @Qualifier로 찾기 쉽습니다.
public class MemberSqlRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberSqlRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setLoginId(rs.getString("login_id"));
            member.setPassword(rs.getString("password"));
            member.setNickname(rs.getString("nickname"));

            // Timestamp -> LocalDateTime 변환
            Timestamp timestamp = rs.getTimestamp("created_at");
            if (timestamp != null) {
                member.setCreatedAt(timestamp.toLocalDateTime());
            }
            return member;
        };
    }

    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO member (login_id, password, nickname, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getLoginId());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getNickname());
            ps.setTimestamp(4, Timestamp.valueOf(member.getCreatedAt())); // LocalDateTime -> Timestamp 변환
            return ps;
        }, keyHolder);

        member.setId(keyHolder.getKey().longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<Member> result = jdbcTemplate.query(sql, memberRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "SELECT * FROM member WHERE login_id = ?";
        List<Member> result = jdbcTemplate.query(sql, memberRowMapper(), loginId);
        return result.stream().findAny();
    }

    @Override
    public void delete(Member member) {
        String sql = "DELETE FROM member WHERE id = ?";
    }
}