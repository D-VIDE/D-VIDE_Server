package com.divide.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String profileImgUrl;
    private String name;
    private String nickname;
    private String address;


//    private LocalDateTime createdAt;
//    private String sexual;
//    private UserRole role;
//    private Address address;
}
