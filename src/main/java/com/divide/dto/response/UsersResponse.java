package com.divide.dto.response;

import com.divide.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UsersResponse {
    private List<User> users;

    public UsersResponse(List<User> users) {
        this.users = users;
    }
}
