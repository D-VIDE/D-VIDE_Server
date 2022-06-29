package com.divide.service;

import com.divide.entity.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {
    List<User> getUsers() throws ExecutionException, InterruptedException;
}
