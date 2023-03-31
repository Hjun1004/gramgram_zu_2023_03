package com.ll.gramgram.base.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 로그인 필요 여부 체크를 위한 @PreAuthorize 애너테이션을 통작하게 해준다.
// 위의 두 어노테이션으로 인해 기존 설정이 동작하지 못하게 함
// 아래의 메서드로 동작해야한다.
// 스프링 시큐리티의 기본설정이 이 클래스로 설정된다. <- 내 생각
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        /*http.authorizeHttpRequests().requestMatchers(
                new AntPathRequestMatcher("/**")).permitAll()
        ;*/ // 위의 명령어는 모든 접근을 허용하게 한다. 라는 의미인데 filterchain 메서드에 아무것도 해주지 않아고 기본적으로 모든 접근에 허용이다.
        http
                /*.authorizeHttpRequests(
                 authorizeHttpRequests -> authorizeHttpRequests
                         .requestMatchers(new AntPathRequestMatcher("/question/list")).permitAll()
                         .requestMatchers(new AntPathRequestMatcher("/style.css")).permitAll()
                         .requestMatchers(new AntPathRequestMatcher("/bootstrap.min.css")).permitAll()
                         .requestMatchers(new AntPathRequestMatcher("/question/detail/**")).permitAll()
                         .requestMatchers(new AntPathRequestMatcher("/user/login")).permitAll()
                         .requestMatchers(new AntPathRequestMatcher("/style.css")).permitAll()
                         .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                         .anyRequest().authenticated() // 위의 페이지들 말고는 다 승인이 필요하다. (로그인이 필요함)
                 )*/
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                                /*.successHandler(new SimpleUrlAuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        if ( isAdmin ) {
                                            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

                                            redirectStrategy.sendRedirect(request, response, "/adm");
                                        }
                                        else {
                                            super.onAuthenticationSuccess(request, response, authentication);
                                        }
                                    }
                                })*/
                                .defaultSuccessUrl("/")
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/member/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)// 쿠키에 있는 세션키를 날린다.
                );

        return http.build();
    }



    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
