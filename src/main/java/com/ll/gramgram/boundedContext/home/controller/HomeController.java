package com.ll.gramgram.boundedContext.home.controller;

import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Enumeration;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/")
    public String showMain(){
        return "usr/home/main";
    }

    @GetMapping("/debugSession")
    @ResponseBody
    public String showDebugSession(HttpSession session){
        StringBuilder sb = new StringBuilder("Session content:\n");

        Enumeration<String> attributeNames = session.getAttributeNames();
        // getAttributeNames 메소드로 세션에 있는 모든 킷값을 가져와 Enumeration객체에 저장
        while(attributeNames.hasMoreElements()){
//        Enumeration객체의 hasMoreElements메소드를 사용하여 아이템이 존재하는지를
//        판단하여 존재하면 반복문을 계속 진행하고 아이템이 존재하지 않으면 반복문은 벗어난다.
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            sb.append(String.format("%s: %s\n",attributeName, attributeValue));
        }

        return sb.toString();
    }
}
