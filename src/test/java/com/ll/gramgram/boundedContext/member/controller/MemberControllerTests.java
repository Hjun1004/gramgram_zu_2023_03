package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // 스프링부트 관련 컴포넌트 테스트할 때 붙여야 함, Ioc 컨테이너 작동시킴
@AutoConfigureMockMvc // http 요청, 응답 테스트
@Transactional // 실제로 테스트에서 발생한 DB 작업이 영구적으로 적용되지 않도록, test + 트랜잭션 => 자동롤백
@ActiveProfiles("test") // application-test.yml 을 활성화 시킨다.
public class MemberControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 폼")
    void t001() throws Exception {
        // WHEN
        ResultActions resultActions = mvc // mvc를 통해서
                .perform(get("/member/join")) //  get 요청으로 수행을 하겠다.  스프링 한테 여기로 접속해라! 라고 시키는것
                .andDo(print()); // 크게 의미 없고, 그냥 확인용

        // THEN // 접속한 결과에 대해서는 이래야 한다고 정의
        resultActions
                .andExpect(handler().handlerType(MemberController.class)) // 핸들러 타입 /member/join으로 맵핑되는 메서드가 있는 컨트롤러가 작동되어야 한다고 정의
                .andExpect(handler().methodName("showJoin")) // /member/join으로 맵핑되는 메서드가 작동되어야 한다고 정의
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="submit" value="회원가입"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("회원가입 폼")
    @Rollback(value = false) // DB에 흔적이 남는다.
    void t002() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // th:action할 때 키가 발생하는데 그것처럼 동작하게 하는것이다.
                        .param("username", "user10")
                        .param("password", "1234")
                )
                .andDo(print());

        // THEN // 접속한 결과에 대해서는 이래야 한다고 정의
        resultActions
                .andExpect(handler().handlerType(MemberController.class)) // 핸들러 타입 /member/join으로 맵핑되는 메서드가 있는 컨트롤러가 작동되어야 한다고 정의
                .andExpect(handler().methodName("join")) // /member/join으로 맵핑되는 메서드가 작동되어야 한다고 정의
                .andExpect(status().is3xxRedirection());

        Member member = memberService.findByUsername("user10").orElse(null);

        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("회원가입시에 올바른 데이터를 넘기지 않으면 400")
    void t003() throws Exception{
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user10")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());



        resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성
                        .param("password", "1234")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());


        resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user10" + "a".repeat(30))
                        .param("password", "1234")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());


        resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user10")
                        .param("password", "1234" + "a".repeat(30))
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("login 폼")
    //@Rollback(value = false) // DB에 흔적이 남는다.
    void t005() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/login")
                        /*.with(csrf()) // th:action할 때 키가 발생하는데 그것처럼 동작하게 하는것이다.
                        .param("username", "user10")
                        .param("password", "1234")*/
                )
                .andDo(print());

        // THEN // 접속한 결과에 대해서는 이래야 한다고 정의
        resultActions
                .andExpect(handler().handlerType(MemberController.class)) // 핸들러 타입 /member/join으로 맵핑되는 메서드가 있는 컨트롤러가 작동되어야 한다고 정의
                .andExpect(handler().methodName("showLogin")) // /member/join으로 맵핑되는 메서드가 작동되어야 한다고 정의
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="submit" value="로그인"
                        """.stripIndent().trim())));

        //Member member = memberService.findByUsername("user10").orElse(null);

        //assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("로그인 처리")
        //@Rollback(value = false) // DB에 흔적이 남는다.
    void t006() throws Exception{
        ResultActions resultActions = mvc
                .perform(post("/member/login")
                        .with(csrf()) // th:action할 때 키가 발생하는데 그것처럼 동작하게 하는것이다.
                        .param("username", "user1")
                        .param("password", "1234")
                )
                .andDo(print());

        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/**"));
    }
}
