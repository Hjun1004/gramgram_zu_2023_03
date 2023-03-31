package com.ll.gramgram.base.initData;

import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
//NotProd: 개발환경이거나, Test환경이 아닐때만 실행
public class NotProd {
    @Bean
    public CommandLineRunner initData(MemberService memberService){
        return args -> {
            memberService.join("admin", "1234");
            memberService.join("user1", "1234");
            memberService.join("user2", "1234");
            memberService.join("user3", "1234");
            memberService.join("user4", "1234");

        };
    }
}
