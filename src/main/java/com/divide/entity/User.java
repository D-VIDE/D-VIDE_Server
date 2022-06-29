package com.divide.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {
    private String id;
    private String email;
    private String password;
    private String profileImgUrl;
    private String name;
    private String nickname;

    public User(String email, String password, String profileImgUrl, String name, String nickname) {
        this.email = email;
        this.password = password;
        this.profileImgUrl = profileImgUrl;
        this.name = name;
        this.nickname = nickname;
    }

    //    private Address address;
//    private UserRole role;
//    private String sexual;
//    private LocalDateTime createdAt;

}
