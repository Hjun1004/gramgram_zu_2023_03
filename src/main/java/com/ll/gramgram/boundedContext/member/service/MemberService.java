package com.ll.gramgram.boundedContext.member.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.memberRepository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hibernate.boot.model.process.spi.MetadataBuildingProcess.build;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 하위에 있는 메서드들은 기본적으로 SELECT할 때 쓰이는것 들
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional // MemberService내의 메서드는 기본적으로 SELECT할 때 쓰여서 @Transactional을 한 번더 붙여서
    // Createfh 쓸 수 있게 해준다.
    public RsData<Member> join(String username, String password) {
        if(findByUsername(username).isPresent()){
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        memberRepository.save(member);

        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }




}
