package com.divide.dto.response;

import com.divide.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UsersResponse extends CommonResponse {
    private List<User> users;

    public UsersResponse(String result, List<User> users) {
        super(result);
        this.users = users;
    }
}
