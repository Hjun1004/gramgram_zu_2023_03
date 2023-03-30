package com.ll.gramgram.boundedContext.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @Column(unique = true)
    private String username;
    private String password;

    public List<? extends GrantedAuthority> getGrantedAuthorities(){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if("admin".equals(username)){
            // UserRole.ADMIN.getValue() == "ROLE_ADMIN"
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }
        else {
            // UserRole.ADMIN.getValue() == "ROLE_USER"
            grantedAuthorities.add(new SimpleGrantedAuthority("member"));
        }
        return grantedAuthorities;
    }

}
