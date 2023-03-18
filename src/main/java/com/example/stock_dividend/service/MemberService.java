package com.example.stock_dividend.service;

import com.example.stock_dividend.exception.impl.AlreadyExistUserException;
import com.example.stock_dividend.exception.impl.UserNotFoundException;
import com.example.stock_dividend.model.Auth;
import com.example.stock_dividend.model.MemberEntity;
import com.example.stock_dividend.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user: " + username));
    }

    public MemberEntity register(Auth.SignUp member){
        boolean exist = this.memberRepository.existsByUsername(member.getUsername());
        if(exist){
            throw new AlreadyExistUserException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());
        return result;
    }


    public MemberEntity authenticate(Auth.SignIn member){
        var user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}
