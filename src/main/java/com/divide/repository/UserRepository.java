package com.divide.repository;

import com.divide.entity.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserRepository {
    List<User> getUsers() throws ExecutionException, InterruptedException;
}
