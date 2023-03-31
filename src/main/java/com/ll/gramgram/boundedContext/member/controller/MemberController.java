package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String showJoin(){
        return "usr/member/join";
    }

    @AllArgsConstructor
    @Getter
    public static class JoinForm{
        @NotBlank
        @Size(min = 4, max = 30)
        private final String username;
        @NotBlank
        @Size(min = 4, max = 30)
        private final String password;
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm){
        memberService.join(joinForm.getUsername(), joinForm.getPassword());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLogin(){
        return "usr/member/login";
    }

    @PostMapping("/login")
    public String showLogin(JoinForm loginForm){
        if(loginForm.getUsername().equals("admin")){
            return "usr/adm/main";
        }
        return "redirect:/";
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/me")
//    public String  showMe(){
//        return "usr/member/me";
//    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String showMain(Model model, Principal principal){
        if(principal != null){
            Member loginedMember = memberService.findByUsername(principal.getName()).orElseThrow();
            model.addAttribute("loginedMember", loginedMember);
        }
        return "usr/member/me";
    }



}
