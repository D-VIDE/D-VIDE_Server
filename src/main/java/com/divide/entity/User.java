package com.divide.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    @Email @NotNull
    private String email;
    @NotNull
    private String password;
    private String profileImgUrl;
    @NotNull
    private String nickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

//    private List<Badge> badges;


    public User( String email, String password, String profileImgUrl, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.profileImgUrl = profileImgUrl;
        this.nickname = nickname;
        this.role = role;
    }

    @CreatedDate
    private LocalDateTime createdAt;

}
