package com.whale.blog.member.service;

import com.whale.blog.member.domain.Member;
import com.whale.blog.member.repository.JpaMemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final JpaMemberRepository memberRepository;

    public MyUserDetailsService(JpaMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<Member> result = memberRepository.findByLoginId(loginId);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 정보입니다");
        }
        Member user = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("일반 유저"));

        return new User(user.getLoginId(), user.getPassword(), authorities);
    }
}

